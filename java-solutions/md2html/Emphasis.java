package md2html;

import java.util.List;

public class Emphasis extends Tagger implements ParagraphElement {

    private final static Tags HTML_TAGS = new Tags("<em>", "</em>");

    public Emphasis(List<? extends ParagraphElement> vars) {
        super(vars);
    }

    public Emphasis(ParagraphElement var) {
        super(var);
    }

    public Emphasis(List<? extends ParagraphElement> vars, Tags raw) {
        super(vars, raw);
    }

    public Emphasis(ParagraphElement var, Tags raw) {
        super(var, raw);
    }

    protected Tags getHTMLTags() {
        return HTML_TAGS;
    }
}