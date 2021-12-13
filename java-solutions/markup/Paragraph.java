package markup;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Paragraph implements Markdownable, ListItemType {

    private final List<Markdownable> jars;

    public Paragraph(List<? extends ParagraphElement> jars) {
        this.jars = new ArrayList<>(jars);
    }

    public Paragraph(ParagraphElement jar) {
        this.jars = Collections.singletonList(jar);
    }

    public List<Markdownable> getContent() {
        return Collections.unmodifiableList(jars);
    }
}