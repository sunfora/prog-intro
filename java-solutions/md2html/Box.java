package md2html;

// Pair with token and tree
public class Box {
    public final Token token;
    public final ParagraphElement tree;
    public Box(Token token, ParagraphElement tree) {
        this.token = token;
        this.tree = tree;
    }

    public Box(Box box) {
        this(box.token, box.tree);
    }

    public String toString() {
        return token.toString();
    }
}