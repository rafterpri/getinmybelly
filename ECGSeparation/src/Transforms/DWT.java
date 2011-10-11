package Transforms;

import Jama.Matrix;

/**
 *
 * @author Lukas
 */
public class DWT {

    // Daubechies D4 wavelet coefficients
    private final static double h0 = (1 + Math.sqrt(3)) / (4 * Math.sqrt(2));
    private final static double h1 = (3 + Math.sqrt(3)) / (4 * Math.sqrt(2));
    private final static double h2 = (3 - Math.sqrt(3)) / (4 * Math.sqrt(2));
    private final static double h3 = (1 - Math.sqrt(3)) / (4 * Math.sqrt(2));
    private static double g0 = h3;
    private static double g1 = -1 * h2;
    private static double g2 = h1;
    private static double g3 = -1 * h0;

    public static double[] discreteHaarWaveletTransform(double[] input) {
        // This function assumes that input.length=2^n, n>1
        double[] output = new double[input.length];

        for (int length = input.length >> 1;; length >>= 1) {
            // length = input.length / 2^n, WITH n INCREASING to log(input.length) / log(2)
            for (int i = 0; i < length; ++i) {
                double sum = input[i * 2] + input[i * 2 + 1];
                double difference = input[i * 2] - input[i * 2 + 1];
                output[i] = sum;
                output[length + i] = difference;
            }
            if (length == 1) {
                return output;
            }

            //Swap arrays to do next iteration
            System.arraycopy(output, 0, input, 0, length << 1);
        }
    }
    
    public static double[] inverseHaarWaveletTransform(double[] input, double weight) {
        Matrix A = new Matrix(input, input.length);
        Matrix W = createW(weight, input.length);
        A = W.inverse().times(A);
        double[][] array = A.transpose().getArray();
        return array[0];
    }

    /**
     * 
     * @param w Transformation Matrix
     * @param s Signal
     * @return the transformed signal
     */
    public static Matrix waveletTransform(Matrix W, Matrix s) {
        return W.times(s);
    }

    /**
     * 
     * @param w the weight for the DWT
     * @param s the input signal
     * @return  the transformed signal
     */
    public static Matrix waveletTransform(double w, Matrix s) {
        Matrix W = createW(w, s.getRowDimension());
        return W.times(s);
    }

    /**
     * 
     * @param w the weight for the DWT
     * @param s the input signal
     * @return the transformed signal
     */
    public static Matrix waveletTransform(double w, double[] s) {
        Matrix W = createW(w, s.length);
        System.out.println("W is created!");
        Matrix Ms = new Matrix(s, s.length);
        return W.times(Ms);
    }

    public static void d4Transform(double[] a) {
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

    public static void invD4Transform(double a[]) {
        int n = a.length;
        if (n >= 4) {
            int i, j;
            int half = n / 2;
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

    /**
     * 
     * @param w weight for the DWT
     * @param n length of the input vector
     * @return 
     */
    public static Matrix createW(double w, int n) {
        double[][] matrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = 0;
            }
        }

        for (int i = 0, j = 0; i < n; i++, j = j + 2) {
            if (i < n / 2) {
                matrix[i][j] = matrix[i][j + 1] = (w / 2);
            } else {
                matrix[i][j - n] = (-w / 2);
                matrix[i][j - n + 1] = (w / 2);
            }
        }
        return new Matrix(matrix);
    }
}
