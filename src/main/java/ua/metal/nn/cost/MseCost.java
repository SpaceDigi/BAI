package ua.metal.nn.cost;

public class MseCost implements ICost {

    private static final MseCost cost = new MseCost();

    public static MseCost getInstance() {
        return cost;
    }

    @Override
    public double cost(double res, double target) {
        return Math.pow(res - target, 2);
    }

    @Override
    public double dCost(double res, double target) {
        return 2 * (res - target);
    }
}
