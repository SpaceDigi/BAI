package ua.metal.nn;



import ua.metal.nn.activation.IActivation;
import ua.metal.nn.cost.ICost;
import ua.metal.nn.exception.BaseException;
import ua.metal.nn.layer.InputLayer;
import ua.metal.nn.layer.Layer;
import ua.metal.nn.layer.OutputLayer;
import ua.metal.nn.util.Edge;

import java.time.LocalDateTime;
import java.util.*;

public class SimpleNeuralNetwork {

    private InputLayer inputLayer;
    private OutputLayer outputLayer;
    private final List<Layer> hiddenLayers;
    private final double lr;
    private final List<Double> losses;

    public SimpleNeuralNetwork(double lr) {
        hiddenLayers = new ArrayList<>();
        losses = new LinkedList<>();
        this.lr = lr;
    }

    public SimpleNeuralNetwork addInputLayer(int inputSize, IActivation activation) {
        inputLayer = new InputLayer(inputSize, activation);
        return this;
    }

    public SimpleNeuralNetwork addOutputLayer(int output, IActivation activation, ICost cost) throws BaseException {
        if (hiddenLayers.isEmpty()) {
            outputLayer = new OutputLayer(output, activation, cost, inputLayer.getInputNeurons(), lr);
            return this;
        }

        outputLayer = new OutputLayer(output, activation, cost, hiddenLayers.get(hiddenLayers.size() - 1), lr);

        return this;
    }

    public SimpleNeuralNetwork addHiddenLayer(int output, IActivation activation) {
        if (hiddenLayers.isEmpty())
            hiddenLayers.add(new Layer(output, activation, lr, inputLayer.getInputNeurons()));
        else
            hiddenLayers.add(new Layer(output, activation, lr, hiddenLayers.get(hiddenLayers.size() - 1)));
        return this;
    }

    public void fit(List<double[]> xList, List<double[]> yList, int epochs, int batchSize) throws BaseException {
        List<Integer> indexArray = new ArrayList<>();
        for (int k = 0; k < xList.size(); k++)
            indexArray.add(k);
        for (int epoch = 0; epoch < epochs; epoch++) {
            double avgLossPerEpoch = 0;
            for (int part = 0; part < xList.size() / batchSize;part++) {
                Collections.shuffle(indexArray);
                List<double[]> yShuffled = new LinkedList<>();
                List<double[]> xShuffled = new LinkedList<>();

                for (Integer index : indexArray) {
                    yShuffled.add(yList.get(index));
                    xShuffled.add(xList.get(index));
                }


                Iterator<double[]> yIterator = yShuffled.iterator();
                Iterator<double[]> xIterator = xShuffled.iterator();
                long timeTotalForward = 0;
                long timeTotalBackward = 0;
                for (int i = 0; i < batchSize; i++) {
                    double[] x = xIterator.next();
                    double[] y = yIterator.next();

                    long start = System.currentTimeMillis();
                    inputLayer.putData(x);
                    hiddenLayers.forEach(Layer::feedForward);
                    outputLayer.feedForward();

                    long end = System.currentTimeMillis();
                    timeTotalForward += (end - start);

                    start = System.currentTimeMillis();

                    outputLayer.backPropagation(y);

                    for (int k = hiddenLayers.size() - 1; k >= 0; k--) {
                        hiddenLayers.get(k).backPropagation();
                    }

                    end = System.currentTimeMillis();
                    timeTotalBackward += (end - start);
                    avgLossPerEpoch += outputLayer.getLastLoss();

                    //System.out.println("(FORWARD): " + timeTotalForward);
                    //System.out.println("(BACKWARD): " + timeTotalBackward);
                    System.gc();
                }
                //System.out.println("[" + LocalDateTime.now().toString() + "] Batch-size: " + batchSize + " totalSize: " + xList.size());
            }
            avgLossPerEpoch /= xList.size();
            losses.add(avgLossPerEpoch);
            System.out.println("[" + LocalDateTime.now().toString() + "] Epoch: " + epoch + " avgLoss: " + avgLossPerEpoch);

            //outputLayer.backPropagation();

           /* for (Layer l : reversedHiddenLayers) {
                l.backPropagation();
            }*/

        }
    }


    public double[] predict(double[] data) throws BaseException {
        inputLayer.putData(data);
        for (Layer l : hiddenLayers) {
            l.feedForward();
        }
        outputLayer.feedForward();
        return outputLayer.predict();
    }


    public String getLearningCoefs() {
        StringBuilder sb = new StringBuilder();
        for (Layer l : hiddenLayers) {
            for (Edge e : l.getEdges().getEdges()) {
                sb.append(e.getWeight()).append("*x + ").append(e.getBias()).append("\n");
            }
            sb.append("New Layer");
        }
        for (Edge e : outputLayer.getEdges().getEdges()) {
            sb.append(e.getWeight()).append("*x + ").append(e.getBias()).append("\n");
        }
        return sb.toString();
    }
}
