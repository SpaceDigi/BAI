package ua.metal.nn.neuron;

import lombok.Setter;
import ua.metal.nn.activation.IActivation;
import ua.metal.nn.activation.SoftmaxActivation;

public class Neuron {

    private IActivation ownActivation;
    private double activatedValue;
    @Setter
    private double clearValue;

    private double dC_dA_1 = 0;

    public Neuron(double value, IActivation ownActivation) {
        this.clearValue = value;
        this.ownActivation = ownActivation;
        activate();
    }

    public Neuron(IActivation ownActivation) {
        clearValue = 0;
        this.ownActivation = ownActivation;
    }

    public void addVal(double v) {
        this.clearValue += v;
    }

    public void activate() {
        activatedValue = ownActivation.activate(clearValue);
    }

    public void activate(double[] inputs) {
        activatedValue = ((SoftmaxActivation) ownActivation).activate(clearValue, inputs);
    }

    public void activate(IActivation activation) {
        activatedValue = activation.activate(clearValue);
    }

    public double aVal() {
        return activatedValue;
    }

    public double zVal() {
        return clearValue;
    }

    public double derOfZVal() {
        return ownActivation.dAct(clearValue);
    }


    public void setDc_dA_1(double dC_dA_1) {
        this.dC_dA_1 += dC_dA_1;
    }

    public void reset() {
        this.clearValue = 0;
        this.activatedValue = 0;
        this.dC_dA_1 = 0;
    }

    public double getDCost() {
        return dC_dA_1;
    }

    @Override
    public String toString() {
        return "Neuron{" +
                ", activatedValue=" + activatedValue +
                ", clearValue=" + clearValue +
                '}';
    }
}
