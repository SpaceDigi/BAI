package ua.metal.nn.layer;

import lombok.Getter;
import ua.metal.nn.activation.IActivation;
import ua.metal.nn.activation.SoftmaxActivation;
import ua.metal.nn.cost.ICost;
import ua.metal.nn.neuron.Neuron;
import ua.metal.nn.util.Edge;
import ua.metal.nn.util.EdgeDataList;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class OutputLayer {

    private int outputSize;
    private IActivation activationFunc;
    private ICost costFunc;

    @Getter
    private EdgeDataList<Edge, Neuron> edges;
    private double learningRate;
    private Double lastLoss;

    @Getter
    private LinkedList<Neuron> outputNeurons;

    private OutputLayer(int outputSize, IActivation activationFunc, ICost costFunc, double lr) {
        this.learningRate = lr;
        this.outputNeurons = new LinkedList<>();
        this.outputSize = outputSize;
        this.activationFunc = activationFunc;
        this.edges = new EdgeDataList<>();
        this.costFunc = costFunc;
        for (int k = 0; k < outputSize; k++)
            outputNeurons.add(new Neuron(activationFunc));

    }

    //Constructor for layer output
    public OutputLayer(int outputSize, IActivation activationFunc, ICost costFunc, Layer previous, double lr) {
        this(outputSize, activationFunc, costFunc, previous.getLayerNeurons(), lr);
    }

    public OutputLayer(int outputSize, IActivation activationFunc, ICost costFunc, List<Neuron> previousNeurons, double lr) {
        this(outputSize, activationFunc, costFunc, lr);
        double bias = Math.random() / outputSize;
        for (Neuron neuronFromPreviousLayer : previousNeurons) {
            for (Neuron neuronFromCurrLayer : outputNeurons) {
                double weight = Math.random();
                edges.add(new Edge(neuronFromPreviousLayer, neuronFromCurrLayer, weight, bias));
            }
        }
    }

    public void feedForward() {
        edges.getEdges().forEach(Edge::reset);
        edges.getEdges().forEach(Edge::feedPropagation);
        if (activationFunc instanceof SoftmaxActivation) {
            double[] inputs = new double[outputSize];
            int k = 0;
            for (Neuron n : outputNeurons)
                inputs[k++] = n.zVal();
            for (Edge e : edges.getEdges())
                e.activate(inputs);
        } else edges.forEach(Edge::activate);
    }

    public void backPropagation(double[] target) {
        Iterator<Neuron> outputIterator = outputNeurons.iterator();
        double tempLoss = 0;
        for (double t : target) {
            Neuron outputNeuron = outputIterator.next();
            tempLoss += costFunc.cost(outputNeuron.aVal(), t);

            double dC_dA = costFunc.dCost(outputNeuron.aVal(), t);
            double dA_dZ;
            if (activationFunc instanceof SoftmaxActivation) dA_dZ = activationFunc.dAct(outputNeuron.aVal());
            else dA_dZ = activationFunc.dAct(outputNeuron.zVal());

            final List<Edge> edgesForThisOutput = edges.getAllEdgesByData(outputNeuron);

            double dA_dZ__dC_dA = dA_dZ * dC_dA;
            double dA_dZ__dC_dA_lr = dA_dZ__dC_dA * learningRate;
            for (Edge e : edgesForThisOutput) {
                double dZ_dW = e.getPrevA();
                //Gradients for current edges
                double dC_dW = dZ_dW * dA_dZ__dC_dA_lr;//Gradient for W
                double dC_dB = dA_dZ__dC_dA_lr;//Gradient for B
                double dC_dA_1 = e.getWeight() * dA_dZ__dC_dA;// dC/dA(L-1)

                e.setDc_dA_1(dC_dA_1);
                e.correlateWeight(dC_dW);
                e.correlateBias(dC_dB);
            }
        }
        //System.out.println("Total loss: " + tempLoss + " avgLoss: " + tempLoss / target.length);
        tempLoss /= target.length;
        lastLoss = tempLoss;
    }

    public double[] predict() {
        double[] res = new double[outputSize];
        int k = 0;
        for (Neuron neuron : outputNeurons)
            res[k++] = neuron.aVal();

        return res;
    }

    public double getLastLoss() {
        if (lastLoss == null)
            return Double.MAX_VALUE;
        return lastLoss;
    }

}
