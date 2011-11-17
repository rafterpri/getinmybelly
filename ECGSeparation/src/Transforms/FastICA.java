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
    private static double[][] weightMatrix;
    private static double[][] B;

    /**
     * Finds a certain number of indepentent components of the inout signal using fastica
     * @param input the input signals
     * @param maxIterations maximum number of iterations
     * @param epsilon level of accuracy
     * @param noComponents the number of components this method should return
     * @return the independent components of the signal
     */
    public static double[][] fastICA(double[][] input, int maxIterations, double epsilon, int noComponents) {
        whitening(input);

        int m = Matrix.getNumOfRows(whitenedVectors);
        int n = Matrix.getNumOfColumns(whitenedVectors);
        for (int c = 0; c < noComponents; c++) {
            double[] w = Vector.random(m);                              //Step 1
            if (B != null) {
                System.out.println("New init...");
                w = Vector.sub(w, Matrix.mult(Matrix.mult(B, Matrix.transpose(B)), w)); // new stuff for step 1
            }
            w = Vector.normalize(w);

            for (int k = 1; k < maxIterations; k++) {               // Steps 2 - 4

                double[] prevW = Arrays.copyOf(w, w.length);

                double[] firstPart = new double[m];
                for (int j = 0; j < n; j++) {

                    //First part of the equation
                    double one = Vector.dot(prevW, Matrix.getVecOfCol(whitenedVectors, j));
                    one = Math.pow(one, 3);
                    double[] two = Vector.scale(one, Matrix.getVecOfCol(whitenedVectors, j));
                    firstPart = Vector.add(firstPart, two);
                    //
                }

                firstPart = Vector.scale((1.0 / (double) n), firstPart);

                //Second part of the equation
                double[] secondPart = Vector.scale(-3, prevW);
                //
                w = Vector.add(firstPart, secondPart);              // End of step 2
                if (B != null) {
                    System.out.println("New Step 3");
                    w = Vector.sub(w, Matrix.mult(Matrix.mult(B, Matrix.transpose(B)), w)); //New stuff for step 3 
                }
                w = Vector.normalize(w);                            // Step 3

                double dotTest = Math.abs(Vector.dot(w, prevW));
                System.out.println("Dot test: " + dotTest);
                if (dotTest >= (1 - epsilon)) {                     // Step 4
                    System.out.println("Converged after " + k + " iterations.");
                    break;
                }
            }

            double[][] wTransposed = new double[m][noComponents];

            for (int i = 0; i < m; i++) {
                wTransposed[i][c] = w[i];
            }
            B = new double[m][c + 1];
            B = wTransposed;
        }
        return Matrix.mult(B, whitenedVectors);
    }

    /**
     * Finds the major component of the input signal using fastica
     * 
     * @param input the input signals
     * @param maxIterations maximum number of iterations
     * @param epsilon level of accuracy
     * @return the major component
     */
    public static double[] fastICA(double[][] input, int maxIterations, double epsilon) {
        whitening(input);

        int m = Matrix.getNumOfRows(whitenedVectors);
        int n = Matrix.getNumOfColumns(whitenedVectors);

        double[] w = Vector.random(m);                              //Step 1
        w = Vector.normalize(w);

        for (int k = 1; k < maxIterations; k++) {               // Steps 2 - 4

            double[] prevW = Arrays.copyOf(w, w.length);

            double[] firstPart = new double[m];
            for (int j = 0; j < n; j++) {

                //First part of the equation
                double one = Vector.dot(prevW, Matrix.getVecOfCol(whitenedVectors, j));
                one = Math.pow(one, 3);
                double[] two = Vector.scale(one, Matrix.getVecOfCol(whitenedVectors, j));
                firstPart = Vector.add(firstPart, two);
                //
            }

            firstPart = Vector.scale((1.0 / (double) n), firstPart);

            //Second part of the equation
            double[] secondPart = Vector.scale(-3, prevW);
            //
            w = Vector.add(firstPart, secondPart);              // End of step 2
            w = Vector.normalize(w);                            // Step 3

            double dotTest = Math.abs(Vector.dot(w, prevW));
            System.out.println("Dot test: " + dotTest);
            if (dotTest >= (1 - epsilon)) {                     // Step 4
                System.out.println("Converged after " + k + " iterations.");
                break;
            }
        }

        double[][] wTransposed = new double[m][1];

        for (int i = 0; i < m; i++) {
            wTransposed[i][0] = w[i];
        }


        return Matrix.mult(wTransposed, whitenedVectors)[0];
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
