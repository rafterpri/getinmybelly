package Transforms;

import Transforms.math.*;
import java.util.Arrays;

/**
 * 
 * @author Tom Pepels
 */
public class FastICA {

    private static double[] meanValues;
    private static double[] eigenValues;
    private static double[][] vectorsZeroMean;
    private static double[][] covarianceMatrix;
    private static double[][] E;
    private static double[][] resVectors;
    private static double[][] whiteningMatrix;
    private static double[][] dewhiteningMatrix;
    private static double[][] whitenedVectors;

    public static void fastICA(double[][] input, int components, int maxIterations) {
        whitening(input);
        double[] w = Vector.random(input[0].length);
        w = Vector.normalize(w);
        for (int k = 1; k < maxIterations; k++) {
            double[][] prevW = Arrays.copyOf(input, input.length);
            
            double[][] one = Matrix.mult(Vector.transpose(prevW), whitenedVectors);
            double[][] two = Matrix.mult(Matrix.square(one), one);
            double[][] three = Matrix.mult(whitenedVectors, two);
        }
    }

    private static void whitening(double[][] input) {
        // Centering, substract the mean from the signal vectors
        meanValues = calcMeanValues(input);
        vectorsZeroMean = Vector.addVecToSet(input, Vector.scale(-1.0, meanValues));
        // calculate the covariance matrix
        covarianceMatrix = Matrix.scale(Matrix.square(vectorsZeroMean), 1.0 / Matrix.getNumOfColumns(input));
        // calculate the eigenvalue decomposition
        EigenValueDecompositionSymm eigenDeco = new EigenValueDecompositionSymm(covarianceMatrix);
        E = eigenDeco.getEigenVectors();
        eigenValues = eigenDeco.getEigenValues();
        // calculate the resulting vectors
        resVectors = Matrix.mult(Matrix.transpose(E), vectorsZeroMean);

        whiteningMatrix =
                Matrix.mult(
                Matrix.diag(Vector.invVector(Matrix.sqrtVector(eigenValues))),
                Matrix.transpose(E));
        dewhiteningMatrix =
                Matrix.mult(E, Matrix.diag(Matrix.sqrtVector(eigenValues)));
        // the whitened vectors' correlation matrix equals unit matrix
        // which is demanded to perform the FastICA algorithm
        whitenedVectors =
                Matrix.mult(whiteningMatrix, vectorsZeroMean);
    }
    

    /**
     * Calculates the mean vector from a set of vectors.
     * @param inVectors the set of vectors
     * @return the resulting mean vector
     */
    private static double[] calcMeanValues(double[][] inVectors) {
        int m = Matrix.getNumOfRows(inVectors);
        int n = Matrix.getNumOfColumns(inVectors);
        double[] mValues = Vector.newVector(m);
        for (int i = 0; i < m; ++i) {
            mValues[i] = 0.0;
            for (int j = 0; j < n; ++j) {
                mValues[i] += inVectors[i][j];
            }
            mValues[i] /= n;
        }
        return (mValues);
    }
}
