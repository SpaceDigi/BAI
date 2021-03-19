package ua.metal.reinforsment;

import java.util.Arrays;
import java.util.Random;

public class Reinforcement {
    public static void main(String[] args) {
        final double[] initProba = {
                0.1, 0.1, 0.05, 0.1, 0.1,
                0.1, 0.1, 0.1, 0.15, 0.1};
        final double[] successProba = {
                0.1, 0.4, 0.2, 0.9, 0.1,
                0.3, 0.5, 0.4, 0.2, 0.3
        };

        final int kol = 1000;
        final double lr = 0.1;
        final double EPS = 1. - 1.E-10;

        int n = initProba.length;

        System.out.println("Props before: ");
        System.out.println(Arrays.toString(initProba));

        int kolOfSuccesses = 0;
        int probsUntilLearned = kol;
        boolean learned = false;
        for (int i = 0; i < kol; i++) {

            int choice = chooseByProps(initProba);
            int success = bernoulliProb(successProba[choice]);
            if (success == 1) {
                kolOfSuccesses++;
                for (int j = 0; j < n; j++) {
                    if (j == choice) {
                        initProba[j] = initProba[j] + lr * (1 - initProba[j]);
                    } else {
                        initProba[j] = initProba[j] - lr * initProba[j];
                    }
                }
            }

            if (!learned) {
                double foundProb = Arrays.stream(initProba).max().getAsDouble();
                if (foundProb > EPS) {
                    learned = true;
                    probsUntilLearned = i;
                }
            }

        }
        System.out.println("Props after: ");
        System.out.println(Arrays.toString(initProba));
        System.out.println("Best action is " + getMaxIndex(successProba));
        System.out.println("Found action: " + getMaxIndex(initProba));
        System.out.println("Max prob: " + Arrays.stream(initProba).max().getAsDouble());
        System.out.println("Success ratio: " + kolOfSuccesses / (double) kol);
        System.out.println("Ratio before learned: " + probsUntilLearned / (double) kol);
    }


    private static int getMaxIndex(double[] array) {
        double max_prob = array[0];
        int index = 0;
        for (int i = 0; i < array.length; i++) {
            if (max_prob < array[i]) {
                index = i;
                max_prob = array[i];
            }
        }
        return index;
    }

    private static int chooseByProps(double[] d) {
        int choice = 0;
        double q = 0;
        Random rnd = new Random();
        double r = rnd.nextDouble();
        for (double v : d) {
            if (v == 0) {
                choice++;
                continue;
            }
            q += v;
            if (q > r) break;
            choice++;
        }
        return choice;
    }

    private static int bernoulliProb(double p) {
        return Math.random() <= p ? 1 : 0;
    }


}
