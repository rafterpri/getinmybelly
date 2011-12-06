package Transforms;

public class ComplexWavelet {

    private static double m;
    private static double fb;
    private static double fc;
    
    public ComplexWavelet(double M, double Fb, double Fc) {
        m = M;
        fb = Fb;
        fc = Fc;
    }
    
    public static Complex[] complexWavelet(double[] input) {
        int n = input.length;
        Complex[] in = new Complex[n];
        
        for (int i = 0; i < n; i++) {
            in[i] = new Complex(input[i], 0); // Complex representation of the input signal.
        }
        Complex[] transform = new Complex[n];
        for (int i = 0; i < n; i++) {
            double sincPart = fb*i/m;
            double second = (Math.sin(sincPart*Math.PI)) / (sincPart*Math.PI); //Sinc function
            second = Math.pow(second, m);
            
            Complex first = new Complex(Math.sqrt(fb),0);
            Complex cSecond = new Complex(second, 0);
            Complex e = new Complex(0, 2*Math.PI*fc*i);
            Complex third = e.exp();
            transform[i] = (first.times(cSecond)).times(third);
        }
        return transform;
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
