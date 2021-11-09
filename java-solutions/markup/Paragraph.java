package markup;

import java.util.List;
import java.util.ArrayList;

public class Paragraph implements ListItemType {

    private final List<ParagraphElement> elems;

    public Paragraph(Iterable<ParagraphElement> vars) {
        elems = new ArrayList<ParagraphElement>();
        for (ParagraphElement var : vars) {
            elems.add(var);
        }
    }

    public Paragraph(List<ParagraphElement> vars) {
    	this((Iterable<ParagraphElement>)vars);
    }	

    public Paragraph(ParagraphElement... vars) {
        this(List.of(vars));
    }

    public void toBBCode(StringBuilder dest) {
        for (ParagraphElement elem : elems) {
            elem.toBBCode(dest);
        }
    }
}