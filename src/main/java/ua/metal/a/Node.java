package ua.metal.a;

public class Node {

    private int index;

    public Node(int index) {
        this.index = index;
    }

    @Override
    public int hashCode() {
        return index;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        else if (!(obj instanceof Node))
            return false;
        Node n = (Node) obj;
        return n.index == index;
    }

    @Override
    public String toString() {
        return "" + index;
    }
}
