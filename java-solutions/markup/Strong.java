package markup;

import java.util.List;

public class Strong extends AbstractBBCodeable implements ParagraphElement  {
    public Strong(Iterable<ParagraphElement> vars) {
        super(vars, Types.STRONG);
    }

    public Strong(List<ParagraphElement> vars) {
        super(vars, Types.STRONG);
    }

    public Strong(ParagraphElement... vars) {
        super(List.of(vars), Types.STRONG);
    }
}