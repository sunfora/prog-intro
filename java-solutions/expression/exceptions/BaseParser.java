package expression.exceptions;

import expression.parser.CharSource;

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

    protected void expect(final char expected) throws ExpectedSymbolException {
        if (!take(expected)) {
            throw new ExpectedSymbolException(pos(), expected, ch);
        }
    }

    protected void expect(final String value) throws ExpectedSymbolException {
        for (final char c : value.toCharArray()) {
            expect(c);
        }
    }

    protected boolean eof() {
        return take(END);
    }

    protected OffsetParseException error(final String message) {
        return new OffsetParseException(pos(), message);
    }

    protected OffsetParseException error(final String message, Throwable cause) {
        return new OffsetParseException(pos(), message, cause);
    }

    protected int pos() {
        return source.pos() + 1;
    }

    protected boolean between(final char from, final char to) {
        return from <= ch && ch <= to;
    }
}
