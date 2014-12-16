package util;

import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

public class Vectors {
    public static double[] subtract(double[] aVec, double[] bVec) {
        return calc(aVec, bVec,
                    (a, b) -> a - b);
    }


    public static double[] divide(double[] aVec, double []bVec) {
        return calc(aVec,
                    bVec,
                    (a, b) -> a / b);
    }

    public static double[] sqaureRoot(double[] vec) {
        return calc(vec,
                    val -> (double) Math.sqrt(val));
    }

    public static double[] square(double[] vec) {
        return calc(vec, val -> val * val);
    }


    public static double[] calc(double[] a, double[] b, BinaryOperator<Double> operator) {
        int len = Math.min(a.length, b.length);
        double []ret = new double[len];
        for (int i = 0; i < len; i++) {
            ret[i] = operator.apply(a[i], b[i]);
        }
        return ret;
    }

    public static double[] calc(double[] a, UnaryOperator<Double> operator) {
        int len = a.length;
        double []ret = new double[len];
        for (int i = 0; i < len; i++) {
            ret[i] = operator.apply(a[i]);
        }
        return ret;
    }

}
