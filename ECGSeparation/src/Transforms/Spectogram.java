package Transforms;

import Transforms.math.BlackmanWindow;
import Transforms.math.Window;

/**
 * Creates a spectogram from given data
 * @author Tom Pepels
 */
public class Spectogram {

    public static double[][] create(int window, double[] signal, int overlap) {
        
        Window w = new BlackmanWindow();
        
        int partitions = signal.length / (window - overlap);// window;
        double[][] specto = new double[partitions][window];
        System.out.println("Partitions: " + partitions);
        
        for (int i = 0; i < partitions; i++) {

            int start = i * (window - overlap);
            int length = window;

            if (start + window > signal.length) {
                break;
            }

            //System.out.println("[" + i + "] Start at: " + start + " end at: " + (start + length));
            
            double[] sourceSignals = new double[length];
            
            System.arraycopy(signal, start, sourceSignals, 0, length);
            sourceSignals = w.timesW(sourceSignals); 
            
            double[] transformedSignals = new double[length];
            transformedSignals = DFT.forward(sourceSignals);
            //transformedSignals = w.timesW(transformedSignals);
            //DWT.d4CompleteInvTransform(sourceSignals);
            //sourceSignals = DWT.discreteHaarWaveletTransform(sourceSignals);
            //System.arraycopy(sourceSignals, 0, specto[i], 0, length);
            System.arraycopy(transformedSignals, length / 2, specto[i], 0, length/2);
        }

        return specto;
    }

    public static double[] inverse(int overlap, double[][] specto) {
        Window w = new BlackmanWindow();
        double[][] spectoNew = specto.clone();
        double[][] spectoN = new double[spectoNew.length][spectoNew[0].length*2];
        for (int i = 0; i < spectoN.length; i++) {
            for (int j = 0; j < spectoN[0].length; j++) {
                if(j<spectoNew[0].length)
                    spectoN[i][j] = spectoNew[i][j];
                else
                    spectoN[i][j] = spectoNew[i][spectoNew[i].length-1-(j-spectoNew[i].length)];
            }
        }
        
        for(int i = 0; i < spectoN.length; i++) {
            
            spectoN[i] = DFT.reverse(specto[i]);
            spectoN[i] = w.inverseW(specto[i]);
        }
        int window = spectoN[0].length*2;
        double [] signal = new double[spectoN.length*(window-overlap)];
        
        for (int i = 0; i < spectoN.length ; i++) {
            int length = spectoN[0].length;
            if((window-overlap)*i+length > signal.length)
                length =signal.length-(window-overlap)*i;
            System.arraycopy(spectoN[i], 0, signal, (window-overlap)*i, length);
        }
        return signal;
    }
}
