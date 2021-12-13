package markup;

import java.util.List;

public class Strikeout extends MarkdownableTagger implements ParagraphElement {

    private final static Tags MARKDOWN_TAGS = new Tags("~", "~");
    private final static Tags BBCODE_TAGS = new Tags("[s]", "[/s]");

    public Strikeout(List<ParagraphElement> vars) {
        super(vars);
    }

    public Strikeout(ParagraphElement var) {
        super(var);
    }

    protected Tags getMarkdownTags() {
        return MARKDOWN_TAGS;
    }

    protected Tags getBBCodeTags() {
        return BBCODE_TAGS;
    }
}