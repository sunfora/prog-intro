package game;

import java.util.Objects;

/**
* Field that can be redacted.
*/
public interface RedactableField<C> extends Field<C> {
    // Setter, may not support null values
    void set(int x, int y, C value);

    // Setter for point
    default void set(IntPair pos, C value) {
        Objects.requireNonNull(pos);
        set(pos.x, pos.y, value);
    }
}
