package ua.metal.nn.cost;

public interface ICost {

    double cost(double res, double target);

    double dCost(double res, double target);

}
