package game;

import java.util.function.Function;
import java.io.IOException;

@FunctionalInterface
interface ReadingFunction<S> {
    public Result<S> apply() throws IOException;
    default <T> ReadingFunction<T> andThen(Function<Result<S>, Result<T>> f) {
        return new ModifyReading<>(this, f);
    }
}