package Transforms;

import Jama.Matrix;
import java.util.Arrays;

/**
 *
 * @author Tom Pepels - 30 september 2011
 */
public class NoiseReduction {

    // Daubechies D4 wavelet coefficients
    private final static double h0 = (1 + Math.sqrt(3)) / (4 * Math.sqrt(2));
    private final static double h1 = (3 + Math.sqrt(3)) / (4 * Math.sqrt(2));
    private final static double h2 = (3 - Math.sqrt(3)) / (4 * Math.sqrt(2));
    private final static double h3 = (1 - Math.sqrt(3)) / (4 * Math.sqrt(2));
    private static double g0 = h3;
    private static double g1 = -1 * h2;
    private static double g2 = h1;
    private static double g3 = -1 * h0;
    
    public static Matrix reduceNoiseHaar(Matrix A, Matrix W, double threshold) {
        A = A.transpose();
        int len = A.getArray()[0].length;
        for (int i = 0; i < len; i++) {
            if (Math.abs(A.get(0, i)) < threshold) {
                A.set(0, i, 0);
            }
        }
        A = A.transpose();
        return (W.inverse().times(A));
    }
    
    public static double[] reduceNoiseD4(double[] signal, double threshold) {
        int len = signal.length;
        double[] sig2 = Arrays.copyOf(signal, len);
        
        d4Transform(sig2);        
        
        for (int i = 0; i < len; i++) {
            if (Math.abs(sig2[i]) < threshold) {
                sig2[i] = 0;
            }
        }
        invD4Transform(sig2);
        return sig2;
    }

    private static void d4Transform(double[] a) {
        int n = a.length;
        if (n >= 4) {
            int i, j;
            int half = n / 2;

            double tmp[] = new double[n];

            i = 0;
            for (j = 0; j < n - 3; j = j + 2) {
                tmp[i] = a[j] * h0 + a[j + 1] * h1 + a[j + 2] * h2 + a[j + 3] * h3;
                tmp[i + half] = a[j] * g0 + a[j + 1] * g1 + a[j + 2] * g2 + a[j + 3] * g3;
                i++;
            }

            tmp[i] = a[n - 2] * h0 + a[n - 1] * h1 + a[0] * h2 + a[1] * h3;
            tmp[i + half] = a[n - 2] * g0 + a[n - 1] * g1 + a[0] * g2 + a[1] * g3;

            for (i = 0; i < n; i++) {
                a[i] = tmp[i];
            }
        }
    }

    private static void invD4Transform(double a[]) {
        int n = a.length;
        if (n >= 4) {
            int i, j;
            int half = n >> 1;
            int halfPls1 = half + 1;

            double tmp[] = new double[n];

            //      last smooth val  last coef.  first smooth  first coef
            tmp[0] = a[half - 1] * h2 + a[n - 1] * g2 + a[0] * h0 + a[half] * g0;
            tmp[1] = a[half - 1] * h3 + a[n - 1] * g3 + a[0] * h1 + a[half] * g1;
            j = 2;
            for (i = 0; i < half - 1; i++) {
                //     smooth val     coef. val       smooth val     coef. val
                tmp[j++] = a[i] * h2 + a[i + half] * g2 + a[i + 1] * h0 + a[i + halfPls1] * g0;
                tmp[j++] = a[i] * h3 + a[i + half] * g3 + a[i + 1] * h1 + a[i + halfPls1] * g1;
            }
            for (i = 0; i < n; i++) {
                a[i] = tmp[i];
            }
        }
    }
}
