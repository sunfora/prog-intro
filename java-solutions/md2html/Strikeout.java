package md2html;

import java.util.List;

public class Strikeout extends Tagger implements ParagraphElement {

    private final static Tags HTML_TAGS = new Tags("<s>", "</s>");

    public Strikeout(List<? extends ParagraphElement> vars) {
        super(vars);
    }

    public Strikeout(ParagraphElement var) {
        super(var);
    }

    public Strikeout(List<? extends ParagraphElement> vars, Tags raw) {
        super(vars, raw);
    }

    public Strikeout(ParagraphElement var, Tags raw) {
        super(var, raw);
    }

    protected Tags getHTMLTags() {
        return HTML_TAGS;
    }
}