package game;

public interface RedactableField extends Field {

    public void set(int x, int y, Cell value);

    default void set(IntPair pos, Cell value) {
        set(pos.x, pos.y, value);
    }
}
