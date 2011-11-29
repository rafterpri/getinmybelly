package Transforms;

/**
 * Creates a spectogram from given data
 * @author Tom Pepels
 */
public class Spectogram {
    public static double[][] create(int window, double[] signal, int overlap) {
        int partitions = signal.length / window;
        double[][] specto = new double[partitions][signal.length / partitions];
        
        for(int i = 0; i < partitions; i++) {
            int offset = i * window;
            double[] fSignals = new double[window];
            System.arraycopy(signal, i * window, fSignals, 0, window);
            fSignals = DFT.forward(fSignals);
            System.arraycopy(fSignals, 0, specto[i], 0, window);
        }
        
        return specto;
    }
    
    public static double[] inverse(int window, double[][] specto) {
        return new double[0];
    }
}
