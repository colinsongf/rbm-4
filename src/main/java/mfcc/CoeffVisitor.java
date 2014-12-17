package mfcc;

public interface CoeffVisitor {
    default void count(int nSamples, int samplePeriod, short sampleSize, short sampleKind) {
    }

    void coefficients(float[] vector);

    default void theEnd(short checksum) {
    }
}
