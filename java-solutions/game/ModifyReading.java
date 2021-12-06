package game;

import java.util.function.Function;
import java.io.IOException;

public class ModifyReading<S, T> implements ReadingFunction<T> {
    Function<Result<S>, Result<T>> g;
    ReadingFunction<S> f;
    public ModifyReading(ReadingFunction<S> f, Function<Result<S>, Result<T>> g) {
        this.g = g;
        this.f = f;
    }

    public Result<T> apply() throws IOException {
        return g.apply(f.apply());
    }
}