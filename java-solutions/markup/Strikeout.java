package markup;

import java.util.List;

public class Strikeout extends AbstractBBCodeable implements ParagraphElement {
    public Strikeout(Iterable<ParagraphElement> vars) {
        super(vars, Types.STRIKEOUT);
    }

    public Strikeout(List<ParagraphElement> vars) {
        super(vars, Types.STRIKEOUT);
    }

    public Strikeout(ParagraphElement... vars) {
        super(List.of(vars), Types.STRIKEOUT);
    }
}