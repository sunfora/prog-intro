package markup;

import java.util.List;

public class ListItem extends AbstractBBCodeable {
    public ListItem(Iterable<ListItemType> vars) {
        super(vars, Types.LIST_ITEM);
    }

    public ListItem(List<ListItemType> vars) {
        super(vars, Types.LIST_ITEM);
    }

    public ListItem(ListItemType... vars) {
        super(List.of(vars), Types.LIST_ITEM);
    }
}