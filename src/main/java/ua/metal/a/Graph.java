package ua.metal.a;

import java.util.*;

import static java.util.Arrays.asList;

public class Graph {

    private final HeuristicFunction heuristicFunction;

    private final Map<Node, List<Branch>> branches = new HashMap<>();
    private final List<Node> fullNodeList = new ArrayList<>();

    public Graph() {
        this(HeuristicFunction.simple());
    }

    public Graph(HeuristicFunction function) {
        this.heuristicFunction = function;
    }

    public void addBranch(Node start, Node end, double weight) {
        if (!fullNodeList.contains(start))
            this.fullNodeList.add(start);
        if (!fullNodeList.contains(end))
            this.fullNodeList.add(end);
        this.branches.putIfAbsent(start, new ArrayList<>());
        this.branches.get(start).add(new Branch(start, end, weight));
        System.out.println("Added connection from " + start + " to " + end);
    }

    public List<Node> findThePath(Node begin, Node target) {
        if (!fullNodeList.contains(begin) || !fullNodeList.contains(target) || branches.get(begin).isEmpty())
            throw new RuntimeException("Path doesn't exists");
        if (begin.equals(target)) {
            return Collections.singletonList(begin);
        } else {
            List<Branch> connectedNodes = branches.get(begin);
            final HashMap<List<Node>, Double> lossMap = new HashMap<>();
            for (Branch branch : connectedNodes) {
                if (branch.end.equals(target))
                    return asList(branch.start, branch.end);

                double cost = heuristicFunction.cost(branch);
                final List<Node> startList = new ArrayList<>();
                startList.add(branch.start);
                startList.add(branch.end);
                if (branches.get(branch.end) == null)
                    continue;
                Result costPerWay = pathTo(cost, target, branches.get(branch.end), startList);
                lossMap.put(costPerWay.nodes, costPerWay.loss);
            }
            Map.Entry<List<Node>, Double> min = Collections.min(lossMap.entrySet(), Comparator.comparing(Map.Entry::getValue));
            return min.getKey();
        }
    }

    private Result pathTo(double previousCost, Node target, List<Branch> connectedBranches, List<Node> path) {
        final List<Result> res = new ArrayList<>();
        for (Branch branch : connectedBranches) {
            //System.out.println("from " + branch.start.getIndex() + " to " + branch.end.getIndex());
            Node end = branch.end;
            double cost = heuristicFunction.cost(branch);
            if (end.equals(target)) {
                path.add(end);
                res.add(new Result(path, cost + previousCost));
            } else if (branches.get(end) != null) {
                List<Node> copyPath = new ArrayList<>(path);
                copyPath.add(end);
                res.add(pathTo(cost + previousCost, target, branches.get(end), copyPath));
            }
        }
        return Collections.min(res, Comparator.comparing(r -> r.loss));
    }

    private class Result {
        private final List<Node> nodes;
        private final double loss;

        private Result(List<Node> nodes, double loss) {
            this.nodes = nodes;
            this.loss = loss;
        }
    }

    public static class Branch {
        private final Node start;
        private final Node end;
        private final double weight;

        private Branch(Node start, Node end, double weight) {
            this.start = start;
            this.end = end;
            this.weight = weight;
        }
    }

    public interface HeuristicFunction {

        double cost(Branch branch);

        static HeuristicFunction simple() {
            return branch -> branch.weight;
        }

    }

    public static void main(String[] args) {
        Graph graph = new Graph();

        final Node node1 = new Node(1);
        final Node node2 = new Node(2);
        final Node node3 = new Node(3);
        final Node node4 = new Node(4);
        final Node node5 = new Node(5);
        graph.addBranch(node1, node2, 1);
        graph.addBranch(node2, node3, 2);
        graph.addBranch(node2, node4, 1);
        graph.addBranch(node3, node4, 1);
        graph.addBranch(node4, node5, 1);
        graph.addBranch(node1, node4, 1);

        System.out.println(graph.findThePath(new Node(1), new Node(5)));
    }

}
