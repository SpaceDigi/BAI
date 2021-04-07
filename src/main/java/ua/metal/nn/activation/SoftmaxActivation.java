package ua.metal.nn.activation;

import java.util.Arrays;

public class SoftmaxActivation implements IActivation {

    private static final SoftmaxActivation activation = new SoftmaxActivation();

    public static SoftmaxActivation getInstance() {
        return activation;
    }

    @Override
    public double activate(double x) {
        throw new NullPointerException();
    }

    public double activate(double input, double[] neuronValues) {
        double total = Arrays.stream(neuronValues).map(Math::exp).sum();
        return Math.exp(input) / total;
    }

    @Override
    public double dAct(double activated) {
        return activated * (1 - activated);
    }
}
