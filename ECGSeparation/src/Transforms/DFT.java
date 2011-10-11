/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Transforms;

/**
 *
 * @author Lukas
 */
public class DFT {
  
    public static double[] DiscreteFourier(double data[]){
        int N = data.length;
        double result[] = new double[N];
        double[] r_data = new double [N];
        double[] i_data = new double [N];
        double p;
        
        for(int i=0; i<N; i++){           
            for (int j=0; j<N; j++) {
                p = 2*Math.PI*i*j/N;
                r_data[i] += data[j] * Math.cos(p);
                i_data[i] -= data[j] * Math.sin(p);
            }       
            r_data[i] /= N;
            i_data[i] /= N;
            result[i] = Math.sqrt(r_data[i]*r_data[i]+i_data[i]*i_data[i]);
        }
        return result;
    }
    
    public static void DiscreteFourier(Complex data[]) {
        int N = data.length;
        double p;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                p = 2 * Math.PI * i * j / N;
                data[i] = new Complex(data[i].real() + data[j].real() * Math.cos(p), data[i].imag() - data[j].real() * Math.sin(p));
            }
            data[i] = data[i].div(new Complex(N, 0));
        }
    }
    
    public static void InvDiscreteFourier(Complex data[]) {
 
    }
}
