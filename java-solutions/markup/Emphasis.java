package markup;

import java.util.List;

public class Emphasis extends AbstractBBCodeable implements ParagraphElement {
    public Emphasis(Iterable<ParagraphElement> vars) {
        super(vars, Types.EMPHASIS);
    }
    public Emphasis(ParagraphElement... vars) {
        super(List.of(vars), Types.EMPHASIS);
    }
}