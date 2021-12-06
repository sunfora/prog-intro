package game;

import java.util.function.Function;

public class Conditional<T> implements Function<Result<T>, Result<T>> {
    Function<T, Boolean> predicate;

    public Conditional(Function<T, Boolean> predicate) {
        this.predicate = predicate;
    }

    public Result<T> apply(Result<T> got) {
        if (got.first && predicate.apply(got.second)) {
            return got;
        }
        return new Result<>(false, got.second);
    }
}