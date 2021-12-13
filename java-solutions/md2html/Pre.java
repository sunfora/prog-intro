package md2html;

import java.util.List;

public class Pre extends Tagger implements ParagraphElement {

    private final static Tags HTML_TAGS = new Tags("<pre>", "</pre>");

    public Pre(List<? extends ParagraphElement> vars) {
        super(vars);
    }

    public Pre(ParagraphElement var) {
        super(var);
    }

    public Pre(List<? extends ParagraphElement> vars, Tags raw) {
        super(vars, raw);
    }

    public Pre(ParagraphElement var, Tags raw) {
        super(var, raw);
    }

    public void toHTML(StringBuilder dest) {
        StringBuilder result = new StringBuilder();
        dest.append(getHTMLTags().open());
        for (HTMLable var: getContent()) {
            var.toRaw(result);
        }
        (new Text(result.toString())).toHTML(dest);
        dest.append(getHTMLTags().close());
    }

    protected Tags getHTMLTags() {
        return HTML_TAGS;
    }
}