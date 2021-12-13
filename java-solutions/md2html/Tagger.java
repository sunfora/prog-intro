package md2html;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public abstract class Tagger implements HTMLable {

    protected List<HTMLable> jars;
    protected Tags raw = new Tags("", "");

    public Tagger(List<? extends HTMLable> jars) {
        this.jars = new ArrayList<>(jars);
    }

    public Tagger(HTMLable jar) {
        this.jars = Collections.singletonList(jar);
    }

    public Tagger(List<? extends HTMLable> jars, Tags raw) {
        this(jars);
        setRawTags(raw);
    }

    public Tagger(HTMLable jar, Tags raw) {
        this(jar);
        setRawTags(raw);
    }

    public void setRawTags(Tags raw) {
        this.raw = raw;
    }

    public Tags getRawTags() {
        return raw;
    }

    protected abstract Tags getHTMLTags();

    public List<? extends HTMLable> getContent() {
        return Collections.unmodifiableList(jars);
    }

    public void toHTML(StringBuilder dest) {
        dest.append(getHTMLTags().open());
        HTMLable.super.toHTML(dest);
        dest.append(getHTMLTags().close());
    }

    public void toRaw(StringBuilder dest) {
        dest.append(getRawTags().open());
        HTMLable.super.toRaw(dest);
        dest.append(getRawTags().close());
    }
}