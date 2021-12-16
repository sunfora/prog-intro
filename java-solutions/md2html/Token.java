package md2html;

import java.util.Arrays;

public class Token {

    private LexType type;
    private StringBuilder buffer = new StringBuilder();

    public Token(LexType type) {
        this.type = type;
    }

    public Token(LexType type, String str) {
        this.type = type;
        buffer.append(str);
    }

    public Token() {
        this.type = LexType.NONE;
    }

    public Token(char c) {
        this.type = LexType.detectType(c);
        this.append(c);
    }

    public Token(String c) {
        this.type = LexType.TEXT;
        buffer.append(c);
    }

    public LexType getType() {
        return type;
    }

    public Token setType(LexType type) {
        this.type = type;
        return this;
    }

    public void append(char c) {
        if (type == LexType.NONE) {
            throw new IllegalStateException("Token of type None can't consume chars");
        }
        buffer.append(c);
    }

    public String toString() {
        return buffer.toString();
    }

    public void setLength(int c) {
        buffer.setLength(c);
    }

    public String substring(int lb) {
        return buffer.substring(lb);
    }

    public String substring(int lb, int rb) {
        return buffer.substring(lb, rb);
    }

    public char charAt(int pos) {
        return buffer.charAt(pos);
    }

    public int length() {
        return buffer.length();
    }
}