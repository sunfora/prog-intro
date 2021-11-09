package markup;

import java.util.List;

public class UnorderedList extends AbstractBBCodeable implements ListItemType {
    public UnorderedList(Iterable<ListItem> vars) {
        super(vars, Types.UNORDERED_LIST);
    }

    public UnorderedList(List<ListItem> vars) {
        super(vars, Types.UNORDERED_LIST);
    }

    public UnorderedList(ListItem... vars) {
        super(List.of(vars), Types.UNORDERED_LIST);
    }
}