package md2html;

import java.util.Collections;
import java.util.List;

public class Text implements ParagraphElement {

    private final StringBuilder text;
    private StringBuilder html;

    public Text(String text) {
        this.text = new StringBuilder(text);
    }

    public List<? extends ParagraphElement> getContent() {
        return Collections.singletonList(this);
    }

    public void toHTML(StringBuilder dest) {
        if (html == null) {
            html = new StringBuilder();
            int j = 0;
            String replace = "";
            for (int i = 0; i < text.length(); ++i) {
                switch (text.charAt(i)) {
                    case '<':
                        replace = "&lt;";
                        break;
                    case '>':
                        replace = "&gt;";
                        break;
                    case '&':
                        replace = "&amp;";
                        break;
                    default:
                        replace = "";
                }
                switch (text.charAt(i)) {
                    case '<': case '>': case '&':
                        html.append(replace);
                        j = i + 1;
                        break;
                    default:
                        html.append(text.charAt(i));
                }
            }
        }
        dest.append(html);
    }

    public void toRaw(StringBuilder dest) {
        dest.append(text);
    }
}