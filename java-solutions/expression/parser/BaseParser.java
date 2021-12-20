package expression.parser;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class BaseParser {
    private static final char END = '\0';
    private final CharSource source;
    protected char ch = 0xffff;

    protected BaseParser(final CharSource source) {
        this.source = source;
        take();
    }

    protected char take() {
        final char result = ch;
        ch = source.hasNext() ? source.next() : END;
        return result;
    }

    protected boolean test(final char expected) {
        return ch == expected;
    }

    protected boolean take(final char expected) {
        if (test(expected)) {
            take();
            return true;
        }
        return false;
    }

    protected boolean expect(final char expected) {
        if (!take(expected)) {
            throw error("expected: " + ch + " found: " + ch);
        }
        return true;
    }

    protected <T> T expect(final char expected, final T throwBack) {
        expect(expected);
        return throwBack;
    }

    protected boolean expect(final String value) {
        for (final char c : value.toCharArray()) {
            expect(c);
        }
        return true;
    }

    protected <T> T expect(final String value, final T throwBack) {
        expect(value);
        return throwBack;
    }

    protected boolean eof() {
        return take(END);
    }

    protected int pos() {
        return source.pos();
    }

    protected IllegalArgumentException error(final String message) {
        return new IllegalArgumentException(source.pos() + ": " + message);
    }

    protected boolean between(final char from, final char to) {
        return from <= ch && ch <= to;
    }
}
