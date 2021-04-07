package ua.metal.nn.activation;

public interface IActivation {

    double activate(double x);

    double dAct(double x);

    static IActivation simple() {
        return new IActivation() {
            @Override
            public double activate(double x) {
                return x;
            }

            @Override
            public double dAct(double x) {
                return 1;
            }
        };
    }

}
