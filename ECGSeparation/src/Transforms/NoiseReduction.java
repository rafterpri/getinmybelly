package Transforms;

import java.util.Arrays;

/**
 *
 * @author Tom Pepels - 30 september 2011
 */
public class NoiseReduction {

    public static double[] reduceNoiseHardT(double signal[], double threshold) {
        int len = signal.length;
        double[] sig2 = Arrays.copyOf(signal, len);

        for (int i = 0; i < len; i++) {
            if (Math.abs(sig2[i]) < threshold) {
                sig2[i] = 0;
            }
        }
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
        
        double[] sorted = bubbleSort(signal);
        double median = Math.abs(sorted[(int)sorted.length/2]);
        double delta = median/0.6745;
        
        double threshold = delta*Math.sqrt(Math.log(signal.length));

        System.out.println("Dynamic Threshold = "+threshold);
        return reduceNoiseHardT(signal, threshold);
    }
    
    public static double[] bubbleSort(double[] signal) {
        double[] input = Arrays.copyOf(signal, signal.length);
        for(int i = 0 ; i < input.length ; i++) {
            for(int j = i ; j < input.length ; j++) {
                if(Math.abs(input[i]) > Math.abs(input[j])) {
                    double help = input[i];
                    input[i] = input[j];
                    input[j] = help;
                }
            }
        }
        return input;
    }
}
