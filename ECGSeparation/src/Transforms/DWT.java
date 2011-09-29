/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Transforms;

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
    
    public static void main(String[] args) {   
        if (Reader.openEDFFile("C:\\ECG.edf") == 0) {
            System.out.println("Open file successful");
        }
        
        if (Reader.noOfSignals() > -1) {
            System.out.println("File has " + Reader.noOfSignals() + " signals.");
        } else {
            System.out.println("Reading number of signals failed.");
        }
        
        String sigName = Reader.signalName(0);
        if (sigName != null) {
            System.out.println("The name of signal 0 is: " + sigName);
        } else {
            System.out.println("Retreiving signal name failed.");
        }
        
        double[] samples = Reader.readSamples(0, 10);
        if (samples.length > 0) {
            System.out.println(samples.length + " samples read.");
            for (int i = 0; i < samples.length; i++) {
                System.out.println("Sample " + i + ": " + samples[i]);
            }
        }
        System.out.println();
        double[] dwt = discreteHaarWaveletTransform(samples);
        for(int i = 0 ; i < dwt.length ; i++) {
            System.out.println("Discrete Haar WT " + i + ": " + dwt[i]);
        }
    }
}
