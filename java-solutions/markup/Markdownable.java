package markup;

import java.util.List;

public interface Markdownable extends BBCodeable {

    public List<? extends Markdownable> getContent();

    default void toMarkdown(StringBuilder dest) {
        for (Markdownable j : getContent()) {
            j.toMarkdown(dest);
        }
    }
}