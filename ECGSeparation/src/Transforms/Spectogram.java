package Transforms;

/**
 * Creates a spectogram from given data
 * @author Tom Pepels
 */
public class Spectogram {
    public static double[][] create(int window, double[] signal, int overlap) {
        int partitions = signal.length /(window-overlap)-overlap;// window;
        double[][] specto = new double[partitions][window];
        
        for(int i = 0; i < partitions; i++) {
            int over = overlap;
//            if (i == 0) {
//                over = 0;
//            }

            int start = i * (window - over);

            int length = window;
            
            if (start + length >= signal.length) {
                length = signal.length - start - 1;
            }
            
            System.out.println("Start at: " + start + " end at: " + (start+length));
            double[] fSignals = new double[length];
            
            System.arraycopy(signal, start, fSignals, 0, length);
            fSignals = DFT.forward(fSignals);
            System.arraycopy(fSignals, 0, specto[i], 0, length);
        }
        
        return specto;
    }
    
    public static double[] inverse(int window, double[][] specto) {
        return new double[0];
    }
}
