package Transforms;

public class ComplexWavelet {
	
	public static double[][] complexCWT(double[][] input) {
		
	}
	
    public static double[] avgSignal(double[][] input) {

        for (int i = 0; i < input.length; i++) {
            input[i] = DFT.forward(input[i]);
        }

        double[] av = new double[input[0].length];
        for (int i = 0; i < av.length; i++) {
            for (int j = 0; j < input.length; j++) {
                av[i] += input[j][i];
            }
            av[i] /= input.length;
        }
        return DFT.reverse(av);
    }
}
