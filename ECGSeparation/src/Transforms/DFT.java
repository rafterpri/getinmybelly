/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Transforms;

import java.util.Arrays;

/**
 *
 * @author Lukas
 */
public class DFT {
    static double r_data[] = null;
    static double i_data[] = null;
   
    public static void DiscreteFourier(double data[]){
        int N = data.length;
        double p;

        r_data = new double [N];
        i_data = new double [N];
       
        for(int i=0; i<N; i++){           
            for (int j=0; j<N; j++) {
                p = 2*Math.PI*i*j/N;
                r_data[i] += data[j] * Math.cos(p);
                i_data[i] -= data[j] * Math.sin(p);
            }       
            r_data[i] /= N;
            i_data[i] /= N;
        }
        data = new double[N];
    }
   
    public static void doFFT(double[] signal) {
            Complex[] cSignal = new Complex[signal.length];
            for(int i = 0 ; i < signal.length ; i++) {
                cSignal[i] = new Complex(signal[i],0);
            }
            fft(cSignal, cSignal.length, 0, new Complex(Math.cos(2*Math.PI/cSignal.length),Math.sin(2*Math.PI/cSignal.length)));
            for(int i = 0 ; i < signal.length ; i++) {
                signal[i] = cSignal[i].real();
            }

    }
    /**
     * Calculates the FFT of Vector a
     * @param a input Vector
     * @param n length of input
     * @param lo first index to be considered
     * @param w to keep track of primitive nth root of unity
     */
    public static void fft(Complex[] a, int n, int lo, Complex w) {
        int i, m;
        Complex z, v, h;

        if (n > 1) {
            m = n / 2;
            z = new Complex(1,0);
            for (i = lo; i < lo + m; i++) {
                h = a[i].minus(a[i + m]);
                a[i] = a[i].minus(a[i + m]);
                a[i+m] = a[i + m]= h.times(z);
                z = z.times(w);
            }
            v = w.times(w);
            fft(a, m, lo, v);
            fft(a, m, lo + m, v);
            shuffle(a, n, lo);
        }
    }
/**
     *  Everyday i'm shuffling
     * @param a imput to be shuffled
     * @param n length
     * @param lo initial index
     */
    public static void shuffle(Complex[] a, int n, int lo) {
        int i, m = n / 2;
        Complex[] b = new Complex[m];

        for (i = 0; i < m; i++) {
            b[i] = a[lo + i];
        }
        for (i = 0; i < m; i++) {
            a[lo + i + i + 1] = a[lo + i + m];
        }
        for (i = 0; i < m; i++) {
            a[lo + i + i] = b[i];
        }
    }
    
    public static void main(String[] args){
        double[] d = {100, 200, 44, 50, 20, 20, 4, 2};
        DiscreteFourier(d);
        for(int i=0; i<r_data.length; i++){
            System.out.println(r_data[i]);
            System.out.println(i_data[i]);
        }
    }
}
