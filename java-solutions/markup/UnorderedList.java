package markup;

import java.util.List;

public class UnorderedList extends Tagger implements ListItemType {
    private final static Tags BBCODE_TAGS = new Tags("[list]", "[/list]");

    public UnorderedList(List<ListItem> vars) {
        super(vars);
    }

    public UnorderedList(ListItem var) {
        super(var);
    }

    protected Tags getBBCodeTags() {
        return BBCODE_TAGS;
    }
}