package game;

import java.util.function.Function;

public class IntDecoder implements Function<String, Pair<Boolean, Integer>> {
    private static final Pair<Boolean, Integer> EMPTY = new Pair<>(false, null);

    public Pair<Boolean, Integer> apply(String token) {
        Integer i;
        try {
            i = Integer.parseInt(token);
        } catch (NumberFormatException e) {
            return EMPTY;
        }
        return new Pair<>(true, i);
    }
}