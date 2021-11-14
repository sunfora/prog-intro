package markup;

import java.util.List;

public class Emphasis extends MarkdownableTagger implements ParagraphElement {

    private final static Tags MARKDOWN_TAGS = new Tags("*", "*");
    private final static Tags BBCODE_TAGS = new Tags("[i]", "[/i]");

    public Emphasis(List<ParagraphElement> vars) {
        super(vars);
    }

    public Emphasis(ParagraphElement var) {
        super(var);
    }

    protected Tags getMarkdownTags() {
        return MARKDOWN_TAGS;
    }

    protected Tags getBBCodeTags() {
        return BBCODE_TAGS;
    }
}