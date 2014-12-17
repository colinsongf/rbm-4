package mfcc;

import java.io.PrintStream;
import java.util.Arrays;

import util.Vectors;

import static util.Vectors.divide;
import static util.Vectors.square;
import static util.Vectors.subtract;

public class CoeffStats implements CoeffVisitor {
    private double []sums;
    private double []squareSums;
    private double []min;
    private double []max;
    private double []counts;

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
        out.println("Count: " + Arrays.toString(counts));
        out.println("Sums: " + Arrays.toString(sums));
        out.println("Square sums: " + Arrays.toString(squareSums));

        out.println("Min: " + Arrays.toString(min));
        out.println("Max: " + Arrays.toString(max));

        out.println("Mean: " + Arrays.toString(getMean()));
        out.println("Deviation: " + Arrays.toString(getDeviation()));
    }

    public double[] getMean() {
        return divide(sums, counts);
    }

    public double[] getDeviation() {
        double []mean = getMean();
        double[] meanSquare = square(mean);
        double[] squareMean = divide(squareSums, counts);
        double[] deviationSquare = subtract(squareMean, meanSquare);
        return Vectors.sqaureRoot(deviationSquare);
    }


    public double[] getSums() {
        return sums;
    }

    public double[] getSquareSums() {
        return squareSums;
    }

    public double[] getMin() {
        return min;
    }

    public double[] getMax() {
        return max;
    }

    public double[] getCounts() {
        return counts;
    }
}
