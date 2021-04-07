package ua.metal.nn.activation;

public class SigmoidActivation implements IActivation {

    private static final SigmoidActivation activation = new SigmoidActivation();

    public static SigmoidActivation getInstance() {
        return activation;
    }

    @Override
    public double activate(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    @Override
    public double dAct(double x) {
        double activated = activate(x);
        return activated * (1 - activated);
    }
}
