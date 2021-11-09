package markup;

public class Text implements ParagraphElement, BBCodeable {
    private final String text;
    public Text(String text) {
        this.text = text;
    }
    public void toBBCode(StringBuilder dest) {
        dest.append(text);
    }
}