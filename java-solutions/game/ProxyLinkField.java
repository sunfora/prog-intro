package game;

// Field that uses another field as source
public class ProxyLinkField<C> implements RedactableField<C> {

    private final RedactableField<Link<C>> field;

    public ProxyLinkField(RedactableField<Link<C>> field) {
        this.field = field;
    }

    @Override
    public void set(int x, int y, C cell) {
        field.set(x, y, new Link<>(cell));
    }

    @Override
    public C get(int x, int y) {
        return field.get(x, y).getObj();
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
}