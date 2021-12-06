package game;

// Simple pair
public class Pair<K, V> {
    public final K first;
    public final V second;

    public Pair(K first, V second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return "Pair(" + first + ", " + second + ")";
    }
}