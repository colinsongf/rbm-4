package mfcc;

public interface MFCVectorVisitor {
    default void count(int n) {

    }

    void coefficients(float[] vector);

    default void theEnd() {

    }
}
