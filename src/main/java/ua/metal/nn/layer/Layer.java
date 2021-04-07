package ua.metal.nn.layer;

import lombok.Getter;
import ua.metal.nn.activation.IActivation;
import ua.metal.nn.neuron.Neuron;
import ua.metal.nn.util.Edge;
import ua.metal.nn.util.EdgeDataList;

import java.util.LinkedList;
import java.util.List;


public class Layer {

    private int outputSize;
    private IActivation activationFunc;

    @Getter
    private EdgeDataList<Edge, Neuron> edges;

    @Getter
    private LinkedList<Neuron> layerNeurons;

    private double learningRate;

    private Layer(int outputSize, IActivation activationFunc, double lr) {
        this.learningRate = lr;
        this.layerNeurons = new LinkedList<>();
        this.outputSize = outputSize;
        this.activationFunc = activationFunc;
        this.edges = new EdgeDataList<>();
        for (int k = 0; k < outputSize; k++)
            layerNeurons.add(new Neuron(activationFunc));

    }

    //Constructor for hidden layer
    public Layer(int outputSize, IActivation activationFunc, double lr, Layer previous) {
        this(outputSize, activationFunc, lr, previous.getLayerNeurons());
    }

    public Layer(int outputSize, IActivation activationFunc, double lr, List<Neuron> previous) {
        this(outputSize, activationFunc, lr);
        double bias = Math.random() / outputSize;
        for (Neuron neuronFromPreviousLayer : previous) {
            for (Neuron neuronFromCurrLayer : layerNeurons) {
                double weight = Math.random();
                edges.add(new Edge(neuronFromPreviousLayer, neuronFromCurrLayer, weight, bias));
            }
        }
    }

    public void feedForward() {
        edges.getEdges().forEach(Edge::reset);

        for (Edge edge : edges.getEdges()) {
            edge.feedPropagation();
        }
        for (Edge edge : edges.getEdges()) {
            edge.activate();
        }
    }

    public void backPropagation() {
        for (Neuron neuron : layerNeurons) {

            double dC_dA = neuron.getDCost();
            double dA_dZ = activationFunc.dAct(neuron.zVal());
            List<Edge> edgesForThisOutput = edges.getAllEdgesByData(neuron);

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
    }

}
