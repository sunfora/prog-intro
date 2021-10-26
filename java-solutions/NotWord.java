public class NotWord extends CharMatcher {
    public NotWord() {
        super();
    }
    public NotWord(NotWord other) {
        super(other);
    }
    @Override
    public void send(char character) {
        boolean isWord = Character.getType(character) == Character.DASH_PUNCTUATION;
        isWord = isWord || Character.isLetter(character);
        isWord = isWord || (character == '\'');
        if (!isWord) {
            cnt++;
            found = true;
        } else {
            reset();
        }
    }
    @Override
    public NotWord clone() {
        return new NotWord(this);
    }
}