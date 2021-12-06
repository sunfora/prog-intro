package game;

public class PrettyProxy implements Prettify {
    Prettify origin;

    public PrettyProxy(Prettify origin) {
        this.origin = origin;
    }
    public String display(Field<?> field) {
        return origin.display(field);
    }
}