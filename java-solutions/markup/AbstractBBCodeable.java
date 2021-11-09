package markup;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public abstract class AbstractBBCodeable implements BBCodeable {

    protected final Types type;
    protected final List<BBCodeable> elements;

    public AbstractBBCodeable(Iterable<? extends BBCodeable> elems, Types type) {
        this.type = type;
        elements = new ArrayList<BBCodeable>();
        for (BBCodeable elem: elems) {
            elements.add(elem);
        }
    }

    public AbstractBBCodeable(BBCodeable arg, Types type) {
        this.type = type;
        elements = Collections.singletonList(arg);
    }

    protected AbstractBBCodeable(Types type) {
        this.type = type;
        elements = Collections.emptyList();
    }

    public void toBBCode(StringBuilder dest) {
        toBBCode(0, dest);
    }

    protected void toBBCode(int already, StringBuilder dest) {
        if (!type.fallThrough) {
            already = 0;
        }
        if (!type.in(already)) {
            dest.append(type.open);
        }
        addContent(set(already, type), dest);
        if (!type.in(already)) {
            dest.append(type.close);
        }
    }

    protected void addContent(int already, StringBuilder dest) {
        for (BBCodeable elem : elements) {
            if (elem instanceof AbstractBBCodeable) {
                ((AbstractBBCodeable)elem).toBBCode(already, dest);
            } else {
                elem.toBBCode(dest);
            }
        }
    }

    protected static enum Types {
        EMPHASIS ("[i]", "[/i]"), STRONG ("[b]", "[/b]"),
        STRIKEOUT ("[s]", "[/s]"), LIST_ITEM ("[*]", ""),
        UNORDERED_LIST ("[list]", "[/list]", false),
        ORDERED_LIST ("[list=1]", "[/list]", false);

        public final String open;
        public final String close;
        public final boolean fallThrough;

        Types(String open, String close, boolean fall) {
            this.open = open;
            this.close = close;
            this.fallThrough = fall;
        }

        Types(String open, String close) {
            this(open, close, true);
        }

        public int toInt() {
            return 1 << ordinal();
        }

        public boolean in(int mask) {
            return ((mask & toInt()) > 0);
        }
    }

    protected int set(int mask, Types type) {
        return mask | type.toInt();
    }
}