package md2html;

import java.util.List;

public class Paragraph extends Tagger {

    private final static Tags HTML_TAGS = new Tags("<p>", "</p>");

    public Paragraph(List<? extends ParagraphElement> vars) {
        super(vars);
    }

    public Paragraph(ParagraphElement var) {
        super(var);
    }

    public Paragraph(List<? extends ParagraphElement> vars, Tags raw) {
        super(vars, raw);
    }

    public Paragraph(ParagraphElement var, Tags raw) {
        super(var, raw);
    }

    protected Tags getHTMLTags() {
        return HTML_TAGS;
    }
}