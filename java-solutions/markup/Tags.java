package markup;

public class Tags {
    private final String open;
    private final String close;

    public Tags(String open, String close) {
        this.open = open;
        this.close = close;
    }

    public String open() {
        return open;
    }
    public String close() {
        return close;
    }
}