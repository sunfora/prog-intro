package md2html;

import java.util.List;

public interface HTMLable {

    public List<? extends HTMLable> getContent();

    default Tags getRawTags() {
        throw new UnsupportedOperationException("Can't set raw tags");
    }
    default void setRawTags(Tags raw) {
        throw new UnsupportedOperationException("Can't set raw tags");
    };
//    public Tags getHTMLTags();

    default void toHTML(StringBuilder dest) {
        for (HTMLable j : getContent()) {
            j.toHTML(dest);
        }
    }

    default void toRaw(StringBuilder dest) {
        for (HTMLable j : getContent()) {
            j.toRaw(dest);
        }
    }
}