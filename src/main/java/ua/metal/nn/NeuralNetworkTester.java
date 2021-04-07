package ua.metal.nn;

import ua.metal.nn.activation.ReluActivation;
import ua.metal.nn.cost.MseCost;
import ua.metal.nn.exception.BaseException;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class NeuralNetworkTester {

    public static void main(String[] args) {
        try {
            SimpleNeuralNetwork network = new SimpleNeuralNetwork(0.0000001);

            network
                    .addInputLayer(2, ReluActivation.getInstance())
                    .addHiddenLayer(4, ReluActivation.getInstance())
                    .addOutputLayer(1, ReluActivation.getInstance(), MseCost.getInstance());


            List<double[]> x = new LinkedList<>();
            List<double[]> y = new LinkedList<>();
            for (int k = 0; k < 100; k++) {
                int i = (int) (Math.random() * 100);
                int j = (int) (Math.random() * 100);
                x.add(new double[]{j, i});
                x.add(new double[]{i, j});
                y.add(new double[]{j + i});
                y.add(new double[]{j + i});
            }

            network.fit(x, y, 150, 100);
            System.out.println(Arrays.toString(network.predict(new double[]{2, 1})));
            System.out.println(Arrays.toString(network.predict(new double[]{6, 4})));
            System.out.println(Arrays.toString(network.predict(new double[]{10, 4})));
            System.out.println(Arrays.toString(network.predict(new double[]{240, 200})));
            System.out.println(Arrays.toString(network.predict(new double[]{200, 240})));
        } catch (BaseException e) {
            e.printStackTrace();
        }
    }

}
