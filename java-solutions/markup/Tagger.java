package markup;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public abstract class Tagger implements BBCodeable {

    List<BBCodeable> jars;

    public Tagger(List<? extends BBCodeable> jars) {
        this.jars = new ArrayList<>(jars);
    }

    public Tagger(BBCodeable jar) {
        this.jars = Collections.singletonList(jar);
    }

    protected abstract Tags getBBCodeTags();

    public List<? extends BBCodeable> getContent() {
        return Collections.unmodifiableList(jars);
    }

    public void toBBCode(StringBuilder dest) {
        dest.append(getBBCodeTags().open());
        BBCodeable.super.toBBCode(dest);
        dest.append(getBBCodeTags().close());
    }
}