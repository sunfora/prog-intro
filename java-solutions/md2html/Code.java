package md2html;

import java.util.List;

public class Code extends Tagger implements ParagraphElement {

    private final static Tags HTML_TAGS = new Tags("<code>", "</code>");

    public Code(List<? extends ParagraphElement> vars) {
        super(vars);
    }

    public Code(ParagraphElement var) {
        super(var);
    }

    public Code(List<? extends ParagraphElement> vars, Tags raw) {
        super(vars, raw);
    }

    public Code(ParagraphElement var, Tags raw) {
        super(var, raw);
    }

    protected Tags getHTMLTags() {
        return HTML_TAGS;
    }
}