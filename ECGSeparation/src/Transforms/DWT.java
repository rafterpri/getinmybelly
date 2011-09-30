/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Transforms;

import Jama.Matrix;
import Signals.Reader;

/**
 *
 * @author Lukas
 */
public class DWT {
    
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
        Matrix W = createW(w,s.getRowDimension());
        return W.times(s);
    }
    /**
     * 
     * @param w the weight for the DWT
     * @param s the input signal
     * @return the transformed signal
     */
     public static Matrix waveletTransform(double w, double[] s) {
        Matrix W = createW(w,s.length);
        System.out.println("W is created!");
        Matrix Ms = new Matrix(s,s.length);
        return W.times(Ms);
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
        
        for (int i = 0, j = 0 ; i < n; i++, j=j+2) {
            if (i < n / 2) {
                matrix[i][j] = matrix[i][j+1] = (w / 2);
            } else {
                matrix[i][j-n] = (-w / 2);
                matrix[i][j-n+1] = (w / 2);
            }
        }
        return new Matrix(matrix);
    }
    
    public static void main(String[] args) {  
        double[] d ={100, 200, 44, 50, 20, 20, 4, 2}; 
        double weight = Math.sqrt(2);
        Matrix result = waveletTransform(weight, d);
        result.print(2, 1);

//        if (Reader.openEDFFile("C:\\ECG.edf") == 0) {
//            System.out.println("Open file successful");
//        }
//        
//        if (Reader.noOfSignals() > -1) {
//            System.out.println("File has " + Reader.noOfSignals() + " signals.");
//        } else {
//            System.out.println("Reading number of signals failed.");
//        }
//        
//        String sigName = Reader.signalName(0);
//        if (sigName != null) {
//            System.out.println("The name of signal 0 is: " + sigName);
//        } else {
//            System.out.println("Retreiving signal name failed.");
//        }
//        
//        double[] samples = Reader.readSamples(0, 10);
//        if (samples.length > 0) {
//            System.out.println(samples.length + " samples read.");
//            for (int i = 0; i < samples.length; i++) {
//                System.out.println("Sample " + i + ": " + samples[i]);
//            }
//        }
//        System.out.println();
//        double[] dwt = discreteHaarWaveletTransform(samples);
//        for(int i = 0 ; i < dwt.length ; i++) {
//            System.out.println("Discrete Haar WT " + i + ": " + dwt[i]);
//        }
    }
}
