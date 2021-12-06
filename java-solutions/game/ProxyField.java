package game;

// Field that uses another field as source
public class ProxyField<C> implements Field<C> {
    private final Field<C> field;

    public ProxyField(Field<C> field) {
        this.field = field;
    }

// #implemented:
    @Override
    public C get(int x, int y) {
        return field.get(x, y);
    }

    @Override
    public int getMinX() {
        return field.getMinX();
    }

    @Override
    public int getMinY() {
        return field.getMinY();
    }

    @Override
    public int getMaxX() {
        return field.getMaxX();
    }

    @Override
    public int getMaxY() {
        return field.getMaxY();
    }

    @Override
    public Range getRangeX() {
        return field.getRangeX();
    }

    @Override
    public Range getRangeY() {
        return field.getRangeY();
    }
// implemented#
}