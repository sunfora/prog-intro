package markup;

import java.util.List;

public class Strong extends MarkdownableTagger implements ParagraphElement {

    private final static Tags MARKDOWN_TAGS = new Tags("__", "__");
    private final static Tags BBCODE_TAGS = new Tags("[b]", "[/b]");

    public Strong(List<ParagraphElement> vars) {
        super(vars);
    }

    public Strong(ParagraphElement var) {
        super(var);
    }

    protected Tags getMarkdownTags() {
        return MARKDOWN_TAGS;
    }

    protected Tags getBBCodeTags() {
        return BBCODE_TAGS;
    }
}