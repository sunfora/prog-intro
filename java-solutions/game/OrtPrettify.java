package game;

import java.util.*;

public class OrtPrettify extends AbstractPrettify<OrtPrettify> {
// #fields:
    protected String sep;
// fields#

// #constructors:
    public OrtPrettify(Field<?> field) {
        super(field);
        sep = "";
    }

    public OrtPrettify() {
        super();
        sep = "";
    }
// constructors#

// #public:
    public OrtPrettify setSep(String sep) {
        this.sep = sep;
        return this;
    }
// public#

// #implemented:
    @Override
    public String display() {
        List<List<String>> cells = createCells();
        IntPair sz = getSizes();
        if (sz.x == 0 || sz.y == 0) {
            return "";
        }
        if (axis) {
            addAxis(cells);
        }
        formatWidth(cells, width);
        StringBuilder table = new StringBuilder();
        for (List<String> row : cells) {
            table.append(String.join(sep, row));
            table.append(System.lineSeparator());
        }
        table.setLength(table.length() - System.lineSeparator().length());
        return table.toString();
    }

    @Override
    public boolean getVertical() {
        return !super.getVertical();
    }

    @Override
    OrtPrettify self() {
        return this;
    }
// implemented#
}