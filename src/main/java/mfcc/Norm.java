package mfcc;

import util.Vectors;

public class Norm implements CoeffVisitor {
    private final double[] mean;
    private final double[] deviation;
    private CoeffVisitor chain;

    public Norm(CoeffStats stats, CoeffVisitor chain) {
        this.chain = chain;
        this.mean = stats.getMean();
        this.deviation = stats.getDeviation();
    }

    @Override
    public void count(int nSamples, int samplePeriod, short sampleSize, short sampleKind) {
        chain.count(nSamples, samplePeriod, sampleSize, sampleKind);
    }

    @Override public void coefficients(float[] vector) {
        double []vec = Vectors.cast(vector);

        double[] aroundZero = Vectors.subtract(vec, mean);
        double[] normalized = Vectors.divide(aroundZero, deviation);

        float[] floatNormalized = Vectors.uncast(normalized);
        chain.coefficients(floatNormalized);
    }

    @Override
    public void theEnd(short checksum) {
        chain.theEnd((short) 0);
    }
}
