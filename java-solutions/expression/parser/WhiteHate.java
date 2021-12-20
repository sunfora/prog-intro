package expression.parser;

// import expression.Util;

public class WhiteHate implements CharSource {

    private CharSource source;
    private boolean lastWasWhite;
    private char c;

    public WhiteHate(CharSource source) {
        this.source = source;
    }

    @Override
    public boolean hasNext() {
        return source.hasNext() || lastWasWhite;
    }

    @Override
    public char next() {
        if (lastWasWhite) {
            lastWasWhite = false;
            return c;
        }
        int whites = 0;
        while (source.hasNext() && Character.isWhitespace(c = source.next())) {
            whites++;
        }
        if (whites > 0) {
            lastWasWhite = !Character.isWhitespace(c);
            return ' ';
        }
        return c;
    }

    @Override
	public int pos() {
		return source.pos();
	}
}