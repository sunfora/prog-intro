package md2html;

public class Tags {
    private final String open;
    private final String close;

    public Tags(Object open, Object close) {
        this(open.toString(), close.toString());
    }

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