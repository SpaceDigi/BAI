package ua.metal.nn.layer;

import lombok.Getter;
import ua.metal.nn.activation.IActivation;
import ua.metal.nn.exception.BaseException;
import ua.metal.nn.neuron.Neuron;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class InputLayer {

    private int outputSize;
    private IActivation activationFunc;

    @Getter
    private List<Neuron> inputNeurons;

    public InputLayer(int outputSize, IActivation activationFunc) {
        this.outputSize = outputSize;
        inputNeurons = new LinkedList<>();
        this.activationFunc = activationFunc;
        for (int k = 0; k < outputSize; k++) {
            inputNeurons.add(new Neuron(activationFunc));
        }
    }

    public void putData(double[] data) throws BaseException {
        if (data.length != inputNeurons.size())
            throw new BaseException("Wrong input dimension");

        Iterator<Neuron> iterator = inputNeurons.iterator();
        for (double d : data) {
            Neuron neuron = iterator.next();
            neuron.setClearValue(d);
            neuron.activate();
        }
    }

}
