package ua.metal.nn.util;

import lombok.Getter;
import lombok.Setter;
import ua.metal.nn.neuron.Neuron;

public class Edge implements GetData<Neuron> {

    private Neuron prevNeuron;
    private Neuron currNeuron;

    @Getter
    @Setter
    private double weight;

    @Getter
    private double bias;

    public Edge(Neuron prevNeuron, Neuron currNeuron, double weight, double bias) {
        this.prevNeuron = prevNeuron;
        this.currNeuron = currNeuron;
        this.weight = weight;
        this.bias = bias;
    }

    public void feedPropagation() {
        double startVal = prevNeuron.aVal();
        double endVal = weight * startVal + bias;
        currNeuron.addVal(endVal);
    }

    public double getPrevA() {
        return prevNeuron.aVal();
    }

    public double getCurrA() {
        return currNeuron.aVal();
    }

    public double getPrevZ() {
        return prevNeuron.zVal();
    }

    public double getCurrZ() {
        return currNeuron.zVal();
    }

    public double getDerActivationEndNeuron() {
        return currNeuron.derOfZVal();
    }

    public void activate() {
        currNeuron.activate();
    }

    public void activate(double[] otherNeuron) {
        currNeuron.activate(otherNeuron);
    }

    public void correlateWeight(double gradient) {
        this.weight += (gradient * -1);
    }

    public void correlateBias(double gradient) {
        this.bias += (gradient * -1);
    }

    @Override
    public Neuron getData() {
        return currNeuron;
    }

    public void setDc_dA_1(double dC_dA_1) {
        prevNeuron.setDc_dA_1(dC_dA_1);
    }

    public void reset() {
        this.currNeuron.reset();
    }
}
