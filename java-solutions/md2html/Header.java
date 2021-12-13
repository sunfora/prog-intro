package md2html;

import java.util.List;

public class Header extends Tagger {

    protected final static Tags TAG_HTML_FORMAT = new Tags("<h%d>", "</h%d>");
    protected Tags HTML_TAGS;

    public Header(List<? extends ParagraphElement> vars) {
        super(vars);
    }

    public Header(ParagraphElement var) {
        super(var);
    }

    public Header(List<? extends ParagraphElement> vars, Tags raw) {
        super(vars, raw);
    }

    public Header(ParagraphElement var, Tags raw) {
        super(var, raw);
    }


    public void toHTML(StringBuilder dest) {
        initTags();
        super.toHTML(dest);
    }

    protected void initTags() {
        Tags raw = getRawTags();
        int level = (raw == null)? 1: raw.open().length();
        if (level > 6 || level < 1) {
            throw new IllegalArgumentException("invalid header level");
        }
        HTML_TAGS = new Tags(
            String.format(TAG_HTML_FORMAT.open(), level),
            String.format(TAG_HTML_FORMAT.close(), level)
        );
    }

    protected Tags getHTMLTags() {
        return HTML_TAGS;
    }
}