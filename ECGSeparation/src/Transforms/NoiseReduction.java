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
}
