package markup;

import java.util.List;

public class OrderedList extends AbstractBBCodeable {
    public OrderedList(Iterable<ListItem> vars) {
        super(vars, Types.ORDERED_LIST);
    }
    public OrderedList(ListItem... vars) {
        super(List.of(vars), Types.ORDERED_LIST);
    }
}