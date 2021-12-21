package expression.exceptions;

import expression.parser.CharSource;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class BaseParser {
    private static final char END = '\0';
    private final CharSource source;
    protected char ch = 0xffff;
    protected char back = ch;

    protected BaseParser(final CharSource source) {
        this.source = source;
        take();
    }

    protected static boolean between(final char test, final char from, final char to) {
        return from <= test && test <= to;
    }

    protected char take() {
        back = ch;
        ch = source.hasNext() ? source.next() : END;
        return back;
    }

    protected boolean betweenBack(final char from, final char to) {
        return between(back, from, to);
    }

    protected boolean testBack(final char c) {
        return back == c;
    }

    protected boolean expectBack(final char expected) throws ExpectedSymbolException {
        if (!testBack(expected)) {
            throw new ExpectedSymbolException(pos() - 1, expected, back);
        }
        return true;
    }

    protected <T> T expectBack(final char expected, final T throwBack) throws ExpectedSymbolException {
        expectBack(expected);
        return throwBack;
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

    protected boolean expect(final char expected) throws ExpectedSymbolException {
        if (!take(expected)) {
            throw new ExpectedSymbolException(pos(), expected, ch);
        }
        return true;
    }

    protected <T> T expect(final char expected, final T throwBack) throws ExpectedSymbolException {
        expect(expected);
        return throwBack;
    }

    protected boolean expect(final String value) throws ExpectedSymbolException {
        for (final char c : value.toCharArray()) {
            expect(c);
        }
        return true;
    }

    protected <T> T expect(final String value, final T throwBack) throws ExpectedSymbolException {
        expect(value);
        return throwBack;
    }

    protected boolean eof() {
        return take(END);
    }

    protected ParseException error(final String message) {
        return new ParseException(pos(), message);
    }

    protected ParseException error(final String message, Throwable cause) {
        return new ParseException(pos(), message, cause);
    }

    protected int pos() {
        return source.pos();
    }

    protected boolean between(final char from, final char to) {
        return between(ch, from, to);
    }

    protected boolean testAny(String syms) {
        for (char sym : syms.toCharArray()) {
            if (test(sym)) {
                return true;
            }
        }
        return false;
    }
}
