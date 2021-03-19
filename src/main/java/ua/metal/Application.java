package ua.metal;

import ua.metal.a.Graph;
import ua.metal.nn.NeuralNetworkTester;
import ua.metal.queens.Queens;
import ua.metal.reinforsment.Reinforcement;

public class Application {

    public static void main(String[] args) {
        System.out.println("A*");
        Graph.main(args);
        System.out.println();

        System.out.println("8 Queens");
        Queens.main(args);

        System.out.println("Reinforcement");
        Reinforcement.main(args);
    }

}
