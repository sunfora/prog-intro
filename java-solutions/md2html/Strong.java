package md2html;

import java.util.List;

public class Strong extends Tagger implements ParagraphElement {

    private final static Tags HTML_TAGS = new Tags("<strong>", "</strong>");

    public Strong(List<? extends ParagraphElement> vars) {
        super(vars);
    }

    public Strong(ParagraphElement var) {
        super(var);
    }

    public Strong(List<? extends ParagraphElement> vars, Tags raw) {
        super(vars, raw);
    }

    public Strong(ParagraphElement var, Tags raw) {
        super(var, raw);
    }

    protected Tags getHTMLTags() {
        return HTML_TAGS;
    }
}