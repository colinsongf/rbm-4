package mfcc;

public interface MFCVectorVisitor {
    default void count(int nSamples, int samplePeriod, short sampleSize, short sampleKind) {
    }

    void coefficients(float[] vector);

    default void theEnd(short checksum) {
    }
}
