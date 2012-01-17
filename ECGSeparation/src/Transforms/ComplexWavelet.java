package Transforms;

import java.util.ArrayList;

public class ComplexWavelet {

    private static double[] rawSignal;
    private static Complex[] rawMaternal;
    private static Complex[] rawFetal;
    private static Complex[] cFetal;
    private static CWT maternalCWT;
    private static CWT fetalCWT;

    public static double[][] complexCWT(double[][] input) {
        // Creating all the preprocessed data and methods that need to be used
        rawSignal = avgSignal(input);
        maternalCWT = new CWT(3, 1, 0.5);
        fetalCWT = new CWT(2, 1, 0.5);

        // Maternal ECG calculation
        rawMaternal = maternalCWT.complexTransform(rawSignal);

        // Fetal ECG calculation
        rawFetal = fetalCWT.complexTransform(rawSignal);

        // Post processing of both signals to detect the overlapped fetal QRS and the rejection of the misdetected QRS points
        return new double[0][0];
    }

    public static double[] complexCWT(double[] thorax, double[] abdomen) {
        // Creating all the preprocessed data and methods that need to be used
        fetalCWT = new CWT(2, 1, 0.5);

        // Maternal ECG calculation

        // Fetal ECG calculation
        rawFetal = fetalCWT.complexTransform(abdomen);
        ArrayList<Integer> peaks = getPeaks(thorax, 0.8);
        cFetal = removeMaternalPeaks(rawFetal, peaks);
        double[] fetal = new double[cFetal.length];
        for (int i = 0; i < cFetal.length; i++) {
            fetal[i] = cFetal[i].mod();
        }
        return fetal;
    }

    public static double[] avgSignal(double[][] input) {

        for (int i = 0; i < input.length; i++) {
            input[i] = DFT.forward(input[i]);
        }

        double[] av = new double[input[0].length];
        for (int i = 0; i < av.length; i++) {
            for (int j = 0; j < input.length; j++) {
                av[i] += input[j][i];
            }
            av[i] /= input.length;
        }
        return DFT.reverse(av);
    }

    public static ArrayList<Integer> getPeaks(double[] input, double threshold) {
        double max = 0;
        double peakThreshold = 0;
        ArrayList<Integer> peaks = new ArrayList<Integer>();
        System.out.println("Start getting the peaks");
        for (int i = 0; i < input.length; i++) {
            if (input[i] > max) {
                max = input[i];
                System.out.println("new max = "+max);
            }
        }
        System.out.println("Max = " + max);
        peakThreshold = max * threshold;

        for (int i = 0; i < input.length; i++) {
            if (input[i] > peakThreshold) {
                int j = i;

                if (input[i] < input[i + 1]) {
                    while (input[j] < input[j + 1] && j>0) {
                        j--;
                    }
                    if (!peaks.contains(j)) {
                        peaks.add(j);
                        System.out.println("Peak at : "+j);
                    }
                } else {
                    while (input[j] > input[j + 1]) {
                        j++;
                    }
                    if (!peaks.contains(j)) {
                        peaks.add(j);
                        System.out.println("Peak at : "+j);
                    }
                }
            }
        }
        return peaks;
    }

    public static Complex[] removeMaternalPeaks(Complex[] input, ArrayList<Integer> peaks) {
        int start;
        int end;
        Complex[] peaksRemoved = input;

        for (int i = 0; i < peaks.size(); i = i + 2) {
            start = peaks.get(i);
            end = peaks.get(i + 1);
            for (int j = start; j < end; j++) {
                peaksRemoved[j] = new Complex(0, 0);
            }
        }
        return peaksRemoved;
    }

    public static double[] postProcess(double[] input) {

        for (int i = 1; i < input.length - 1; i++) {
            if ((input[i + 1] - input[i]) > (1.5 * (input[i] - input[i - 1]))) {
                // Possible overlap
            } else if ((input[i + 1] - input[i]) < (1.5 * (input[i] - input[i - 1])) && ((input[i + 1] - input[i]) > (0.45 * (input[i] - input[i - 1])))) {
                // Continue with next peak
            } else if ((input[i + 1] - input[i]) > (0.45 * (input[i] - input[i - 1]))) {
                // Misdetected fetal QRS exists
            }
        }
        return new double[0];
    }
}
