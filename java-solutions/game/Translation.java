package game;

import java.util.function.Function;

public class Translation<S, T> implements Function<Result<S>, Result<T>> {
    Function<S, T> f;
    public Translation(Function<S, T> f) {
        this.f = f;
    }

    public Result<T> apply(Result<S> got) {
        return new Result<>(got.first, f.apply(got.second));
    }
}