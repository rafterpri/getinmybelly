package Transforms;

import Jama.Matrix;

/**
 *
 * @author Tom Pepels - 30 september 2011
 */
public class NoiseReduction {
    
    /**
     * 
     * @param A Matrix A in WaveTransform domain
     * @param W Matrix W the DWT matrix used to transform X
     * @param threshold the noise reduction threshold
     */
    public static Matrix reduceNoise(Matrix A, Matrix W, double threshold) {
        for(int i = 0; i < A.getArray()[0].length; i++) {
            if (A.get(0, i) < threshold) {
                A.set(0, i, 0);
            }
        }
        // Matrix T = A.transpose();
        return (W.inverse().times(A));
    }
}

