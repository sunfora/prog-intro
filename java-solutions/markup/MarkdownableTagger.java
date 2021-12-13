package markup;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public abstract class MarkdownableTagger extends Tagger implements Markdownable {

    List<Markdownable> jars;

    public MarkdownableTagger(List<? extends Markdownable> elems) {
        super(elems);
        jars = new ArrayList<>(elems);
    }

    public MarkdownableTagger(Markdownable elem) {
        super(elem);
        jars = Collections.singletonList(elem);
    }

    public List<? extends Markdownable> getContent() {
        return Collections.unmodifiableList(jars);
    }

    protected abstract Tags getMarkdownTags();

    public void toMarkdown(StringBuilder dest) {
        dest.append(getMarkdownTags().open());
        Markdownable.super.toMarkdown(dest);
        dest.append(getMarkdownTags().close());
    }
}