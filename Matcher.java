package soup.util;

/**
 * Matcher to match sequences of characters in the input.
 * reset() method resets Matcher as it was not used
 * send() method sends new character to Matcher to update its state
 * found() method indicates that pattern found
 * matchSize() method returns current match length
 */
public interface Matcher {
    public void reset();
    public void send(char character);
    public boolean found();
    public int matchSize();
    public Matcher clone();
}