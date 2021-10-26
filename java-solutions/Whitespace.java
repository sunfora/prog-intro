public class Whitespace extends CharMatcher {
    public Whitespace() {
        super();
    }
    public Whitespace(Whitespace other) {
        super(other);
    }
    @Override
    public void send(char character) {
        boolean isWhite = Character.isWhitespace(character);
        if (isWhite) {
            cnt++;
            found = true;
        } else {
            reset();
        }
    }
    @Override
    public Whitespace clone() {
        return new Whitespace(this);
    }
}