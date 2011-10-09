package Transforms;

import java.util.Arrays;

/**
 *
 * @author Tom Pepels - 30 september 2011
 */
public class NoiseReduction {

    private static double median(double[] input) {
        return input[(input.length / 2)];
    }

    /**
     * Smoothes the signal by substituting the each value with the median of nearest n values.
     * @param signal
     * @param n 2k+1!!!
     * @return 
     */
    public static double[] medianFilter(double signal[], int n) {
        double[] output = Arrays.copyOf(signal, signal.length);

        for (int i = 0; i < signal.length; i++) {
            double[] m = new double[n];
            int l = 0;

            // At the start of the signal array use the first values multiple times
            if (i < (n / 2)) {

                while (l < (n / 2)) {
                    m[l] = signal[i];
                    l++;
                }

                for (int k = i; k < i + (n / 2); k++) {
                    m[l] = signal[k];
                    l++;
                }
            } else if ((i + n / 2) >= signal.length) {

                for (int k = i - (n / 2); k <= i; k++) {
                    m[l] = signal[k];
                    l++;
                }

                while (l < (n / 2)) {
                    m[l] = signal[i];
                    l++;
                }

            } else {
                for (int k = i - (n / 2); k < i + (n / 2); k++) {
                    m[l] = signal[k];
                    l++;
                }
            }
            inPlaceBubbleSort(m);
            output[i] = median(m);
        }
        return output;
    }

    public static double[] reduceNoiseHardT(double signal[], double threshold) {
        int len = signal.length;
        int count = 0;
        double[] sig2 = Arrays.copyOf(signal, len);

        for (int i = 0; i < len; i++) {
            if (Math.abs(sig2[i]) < threshold) {
                sig2[i] = 0;
                count++;
            }
        }
        System.out.println(count + " signals set to 0");
        return sig2;
    }

    public static double[] reduceNoiseSoftT(double signal[], double threshold) {
        int len = signal.length;
        double[] sig2 = Arrays.copyOf(signal, len);

        for (int i = 0; i < len; i++) {
            if (Math.abs(sig2[i]) < threshold) {
                System.out.println("zero!");
                sig2[i] = 0;
            } else {
                if (sig2[i] > 0) {
                    System.out.println("minus t");
                    sig2[i] -= threshold;
                } else {
                    System.out.println("plus t");
                    sig2[i] += threshold;
                }
            }
        }
        return sig2;
    }

    public static double[] reduceNoiseDynamicT(double signal[]) {

        double[] sorted = absoluteBubbleSort(signal);
        double median = Math.abs(sorted[(int) ((sorted.length) / 4)]);
        double delta = median / 0.6745;

        double threshold = delta * Math.sqrt(Math.log(signal.length));

        System.out.println("Dynamic Threshold = " + threshold);
        return reduceNoiseHardT(signal, threshold);
    }

    public static double[] reduceNoiseTomsWay(double signal[]) {
        double[] inputSecondPart = Arrays.copyOfRange(signal, signal.length / 2, signal.length);
        double[] inputFirstPart = Arrays.copyOfRange(signal, 0, (signal.length / 2));
        double[] sorted = absoluteBubbleSort(inputSecondPart);
        double median = Math.abs(sorted[(int) sorted.length / 2]);
        double delta = median / 0.6745;

        double threshold = delta * Math.sqrt(Math.log(inputSecondPart.length));

        System.out.println("Dynamic Threshold = " + threshold);
        double[] noiseReducedSecondPart = reduceNoiseHardT(inputSecondPart, threshold);
        double[] output = new double[signal.length];
        for (int i = 0; i < signal.length; i++) {
            if (i < signal.length / 2) {
                output[i] = inputFirstPart[i];
            } else {
                output[i] = noiseReducedSecondPart[i - signal.length / 2];
            }

        }
        return output;
    }

    public static double[] absoluteBubbleSort(double[] signal) {
        double[] input = Arrays.copyOf(signal, signal.length);
        for (int i = 0; i < input.length; i++) {
            input[i] = Math.abs(input[i]);
        }

        for (int i = 0; i < input.length; i++) {
            for (int j = i; j < input.length; j++) {
                if (Math.abs(input[i]) > Math.abs(input[j])) {
                    double help = input[i];
                    input[i] = input[j];
                    input[j] = help;
                }
            }
        }
        return input;
    }

    /**
     * Sorts an array without copying it
     * @param input 
     */
    public static void inPlaceBubbleSort(double[] input) {
        for (int i = 0; i < input.length; i++) {
            for (int j = i; j < input.length; j++) {
                if (input[i] > input[j]) {
                    double help = input[i];
                    input[i] = input[j];
                    input[j] = help;
                }
            }
        }
    }
}
