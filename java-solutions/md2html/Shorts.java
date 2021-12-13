package md2html;
public class Shorts {
    private Shorts() {};
// #shorts:
    static int len(Box box) {
        return box.token.length();
    }

    static int len(Token token) {
        return token.length();
    }

    static LexType type(Box box) {
        return box.token.getType();
    }

    static LexType type(Token token) {
        return token.getType();
    }

    static ParagraphElement tree(Token token) {
        return new Text(token.toString());
    }

    static ParagraphElement tree(Box box) {
        if (box.tree == null) {
            return (ParagraphElement) tree(box.token);
        }
        return box.tree;
    }
// shorts#
}