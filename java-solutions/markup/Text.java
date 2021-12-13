package markup;

import java.util.Collections;
import java.util.List;

public class Text implements ParagraphElement {
    private final String text;

    public Text(String text) {
        this.text = text;
    }

    public List<? extends Markdownable> getContent() {
        return Collections.singletonList(this);
    }

    public void toBBCode(StringBuilder dest) {
        dest.append(text);
    }

    public void toMarkdown(StringBuilder dest) {
        dest.append(text);
    }

}