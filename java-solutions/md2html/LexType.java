package md2html;

public enum LexType {
    UNDERSCORE, STAR, DASH, CODE, NL, HASH, SPACE, TEXT, NONE;

    public static LexType detectType(char c) {
        switch (c) {
            case '\f':
            case '\n':
            case '\r':
            case '\u000b':
            case '\u2028':
            case '\u2029':
            case '\u0085':
                return LexType.NL;
            case '*':
                return LexType.STAR;
            case '-':
                return LexType.DASH;
            case '_':
                return LexType.UNDERSCORE;
            case '`':
                return LexType.CODE;
            case '#':
                return LexType.HASH;
            default:
                if (Character.isWhitespace(c)) {
                    return LexType.SPACE;
                }
                return LexType.TEXT;
        }
    }
}