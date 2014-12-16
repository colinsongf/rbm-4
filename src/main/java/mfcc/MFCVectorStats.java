package mfcc;

import java.io.PrintStream;
import java.util.Arrays;

import util.Vectors;

import static util.Vectors.divide;
import static util.Vectors.square;
import static util.Vectors.subtract;

public class MFCVectorStats implements MFCVectorVisitor {
    public double []sums;
    public double []squareSums;
    public double []min;
    public double []max;
    public double []counts;

    @Override
    public void count(int n) {
        sums = new double[MFC.N_COEFFICIENTS];
        squareSums = new double[MFC.N_COEFFICIENTS];
        min = new double[MFC.N_COEFFICIENTS];
        max = new double[MFC.N_COEFFICIENTS];
        counts = new double[MFC.N_COEFFICIENTS];

        Arrays.fill(min, Float.MAX_VALUE);
        Arrays.fill(max, Float.MIN_VALUE);
    }

    @Override
    public void coefficients(float[] vector) {
        for (int i = 0; i < MFC.N_COEFFICIENTS; i++) {
            float val = vector[i];

            if (Float.isNaN(val) || Float.isInfinite(val)) {
                continue;
            }

            sums[i] += val;
            squareSums[i] += val * val;
            min[i] = Math.min(val, min[i]);
            max[i] = Math.max(val, max[i]);
            counts[i]++;
        }

    }

    public void output(PrintStream out) {
        System.out.println("Count: " + Arrays.toString(counts));
        System.out.println("Sums: " + Arrays.toString(sums));
        System.out.println("Square sums: " + Arrays.toString(squareSums));

        System.out.println("Min: " + Arrays.toString(min));
        System.out.println("Max: " + Arrays.toString(max));

        double[] mean = divide(sums, counts);
        double[] meanSquare = square(mean);
        double[] squareMean = divide(squareSums, counts);
        double[] deviationSquare = subtract(squareMean, meanSquare);
        double[] deviation = Vectors.sqaureRoot(deviationSquare);

        System.out.println("Mean: " + Arrays.toString(mean));
        System.out.println("Deviation: " + Arrays.toString(deviation));
    }

}
