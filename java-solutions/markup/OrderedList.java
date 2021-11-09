package markup;

import java.util.List;

public class OrderedList extends AbstractBBCodeable implements ListItemType {
    public OrderedList(Iterable<ListItem> vars) {
        super(vars, Types.ORDERED_LIST);
    }

    public OrderedList(List<ListItem> vars) {
        super(vars, Types.ORDERED_LIST);
    }

    public OrderedList(ListItem... vars) {
        super(List.of(vars), Types.ORDERED_LIST);
    }
}