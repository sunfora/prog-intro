package game;

public class Result<S> extends Pair<Boolean, S> {
    public Result(boolean success, S value) {
        super(success, value);
    }

    public boolean isValid() {
        return first;
    }

    public S getValue() {
        return second;
    }

    public String toString() {
        return String.format("FunctionResult<%d  %d>", first, second);
    }
}