package Transforms;

public class ComplexWavelet {
	
	private static double[] rawSignal;
	private static CWT maternalCWT;
	private static CWT fetalCWT;
	
	public static double[][] complexCWT(double[][] input) {
		// Creating all the Data and methods that need to be used
		rawSignal = avgSignal(input);
		maternalCWT = new CWT(3,1,0.5);
		fetalCWT = new CWT(1,1,0.5);
		
		// Maternal ECG calculation
		CWT.complexTransform(rawSignal);
		
		// Fetal ECG calculation
		CWT.complexTransform(rawSignal);
		
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
