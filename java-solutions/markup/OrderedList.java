package markup;

import java.util.List;

public class OrderedList extends Tagger implements ListItemType {
    private final static Tags BBCODE_TAGS = new Tags("[list=1]", "[/list]");

    public OrderedList(List<ListItem> vars) {
        super(vars);
    }

    public OrderedList(ListItem var) {
        super(var);
    }

    protected Tags getBBCodeTags() {
        return BBCODE_TAGS;
    }
}