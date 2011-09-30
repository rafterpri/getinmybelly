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
        A = A.transpose();
        int len = A.getArray()[0].length;
        for(int i = 0; i < len; i++) {
            if (Math.abs(A.get(0, i)) < threshold) {
                A.set(0, i, 0);
            }
        }
        A = A.transpose();
        return (W.inverse().times(A));
    }
}

