package mfcc;

import java.io.PrintStream;
import java.util.Arrays;

public class MFCVectorStats implements MFCVectorVisitor {
    private float []sums;
    private float []squareSums;
    private float []min;
    private float []max;
    private int []counts;

    @Override
    public void count(int n) {
        sums = new float[MFC.N_COEFFICIENTS];
        squareSums = new float[MFC.N_COEFFICIENTS];
        min = new float[MFC.N_COEFFICIENTS];
        max = new float[MFC.N_COEFFICIENTS];
        counts = new int[MFC.N_COEFFICIENTS];

        Arrays.fill(min, Float.MAX_VALUE);
        Arrays.fill(max, Float.MIN_VALUE);
    }

    @Override
    public void coefficients(float[] vector) {
        for (int i = 0; i < MFC.N_COEFFICIENTS; i++) {
            if (Float.isNaN(vector[i])) {
                continue;
            }

            sums[i] += vector[i];
            squareSums[i] += vector[i] * vector[i];
            min[i] = Math.min(vector[i], min[i]);
            max[i] = Math.max(vector[i], max[i]);
            counts[i]++;
        }

    }

    public void output(PrintStream out) {
        System.out.println("Count: " + Arrays.toString(counts));
        System.out.println("Sums: " + Arrays.toString(sums));
        System.out.println("Avgs: " + Arrays.toString(squareSums));
        System.out.println("Min: " + Arrays.toString(min));
        System.out.println("Max: " + Arrays.toString(max));
        System.out.println("Square sums: " + Arrays.toString(divide(sums, counts)));
        System.out.println("Square sums avgs: " + Arrays.toString(divide(squareSums, counts)));
    }

    private float[] divide(float[] vals, int []counts) {
        float []ret = new float[vals.length];
        for (int i = 0; i < vals.length; i++) {
            ret[i] = vals[i] / counts[i];
        }
        return ret;
    }
}
