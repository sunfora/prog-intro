package markup;

import java.util.List;

public class ListItem extends Tagger {

    private final static Tags BBCODE_TAGS = new Tags("[*]", "");

    public ListItem(List<ListItemType> vars) {
        super(vars);
    }

    public ListItem(ListItemType var) {
        super(var);
    }

    protected Tags getBBCodeTags() {
        return BBCODE_TAGS;
    }
}