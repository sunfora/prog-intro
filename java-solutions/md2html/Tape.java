package md2html;

import java.util.*;

// Extended ArrayList with more functionality specially for Parser
public class Tape extends ArrayList<Box> {
    public Tape() {
        super();
    }

    public Box back() {
        return get(size() - 1);
    }

    public Box pop() {
        return remove(size() - 1);
    }

    public void add(Token token) {
        add(new Box(token, null));
    }

    public Token getToken(int id) {
        return get(id).token;
    }

    public ParagraphElement getTree(int id) {
        return Shorts.tree(get(id));
    }

    public List<ParagraphElement> uncover(Range range) {
        ArrayList<ParagraphElement> exchange = new ArrayList<>();
        for (int i = range.inf; i < range.sup; ++i) {
            exchange.add(getTree(i));
        }
        return exchange;
    }

    public void replace(Range range, Collection<? extends Box> rep) {
        if (range.empty) {
            return;
        }
        rep = new ArrayList<>(rep);
        List<Box> sub = subList(range.inf, range.sup);
        sub.clear();
        sub.addAll(rep);
    }
}