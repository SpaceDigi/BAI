package ua.metal.nn.activation;

public class ReluActivation implements IActivation {

    private static final ReluActivation activation = new ReluActivation();

    public static ReluActivation getInstance() {
        return activation;
    }

    @Override
    public double activate(double x) {
        return Math.max(0, x);
    }

    @Override
    public double dAct(double x) {
        if (x > 0)
            return 1;
        return 0;
    }
}
