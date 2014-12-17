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
    public void count(int nSamples, int samplePeriod, short sampleSize, short sampleKind) {
        int n = sampleSize / 4;
        sums = new double[n];
        squareSums = new double[n];
        min = new double[n];
        max = new double[n];
        counts = new double[n];

        Arrays.fill(min, Float.MAX_VALUE);
        Arrays.fill(max, Float.MIN_VALUE);
    }

    @Override
    public void coefficients(float[] vector) {
        for (int i = 0; i < vector.length; i++) {
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
