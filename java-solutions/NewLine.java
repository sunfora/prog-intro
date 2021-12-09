public class NewLine extends CharMatcher {
    public NewLine() {
        super();
    }
    public NewLine(NewLine other) {
        super(other);
    }
    @Override
    public void send(char character) {
        if (found) {
            found = false;
            cnt = 0;
        }
	if (character == System.lineSeparator().charAt(cnt)) {
            ++cnt;
        }
	found = cnt == System.lineSeparator().length();
        last = character;
    }
    @Override
    public NewLine clone() {
        return new NewLine(this);
    }
}
