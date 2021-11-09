package markup;

import java.util.List;

public class ListItem extends AbstractBBCodeable {
    public ListItem(Iterable<BBCodeable> vars) {
        super(vars, Types.LIST_ITEM);
    }
    public ListItem(BBCodeable... vars) {
        super(List.of(vars), Types.LIST_ITEM);
    }
}