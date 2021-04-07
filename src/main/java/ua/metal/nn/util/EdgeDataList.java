package ua.metal.nn.util;

import lombok.Getter;

import java.util.*;
import java.util.function.Consumer;

public class EdgeDataList<E extends GetData<N>, N> {

    @Getter
    private LinkedList<E> edges;
    private HashMap<N, List<E>> edgesByData;

    public EdgeDataList() {
        edges = new LinkedList<>();
        edgesByData = new HashMap<>();
    }

    public void add(E edge) {
        this.edges.add(edge);
        edgesByData.putIfAbsent(edge.getData(), new ArrayList<>());
        edgesByData.get(edge.getData()).add(edge);
    }

    public List<E> getAllEdgesByData(N n) {
        return edgesByData.get(n);
    }

    public void forEach(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        for (E t : edges) {
            action.accept(t);
        }
    }

}
