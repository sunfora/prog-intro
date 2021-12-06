package game;

import java.util.*;

public abstract class AbstractPrettify<T extends AbstractPrettify<T>> implements Prettify {
// #fields: /*FOLD00*/
    protected Field<?> field;
    // Maps
    protected Map<IntPair, String> concrete;
    protected Map<?, String> universal;
    // Params
    protected boolean axis;
    protected boolean vertical;
    protected boolean horizontal;
    protected int width;
    // Used
    protected static final String WIDTH_FORMAT = "%%%ds";
// fields# /*FOLD00*/

// #constructors: /*FOLD00*/
    public AbstractPrettify(Field<?> field) {
        this.field = Objects.requireNonNull(field);
        setToDefault();
    }

    public AbstractPrettify() {
        this(new BaseField<Object>(0, 0));
    }
// constructors# /*FOLD00*/

// #public: /*FOLD00*/
    public T flipHorizontal() {
        horizontal = (horizontal == false);
        return self();
    }

    public T flipVertical() {
        vertical = (vertical == false);
        return self();
    }

    public T setField(Field<?> field) {
        this.field = field;
        return self();
    }

    public AbstractPrettify showAxis() {
        return showAxis(true);
    }

    public T showAxis(boolean value) {
        this.axis = value;
        return self();
    }

    public T mapByObject(Map<?, String> map) {
        this.universal = Map.copyOf(map);
        return self();
    }

    public T mapByPoint(Map<IntPair, String> map) {
        this.concrete = Map.copyOf(map);
        return self();
    }

    public T setWidth(int width) {
        assert width >= 0 : "negative width";
        this.width = width;
        return self();
    }

    public String display(Field<?> field) {
        Field old = this.field;
        Object obj = field;
        Objects.requireNonNull(obj);
        this.field = field;
        return Util.fall(display(), this.field = old);
    }

    public T setToDefault() {
        this.axis = true;
        this.width = 1;
        this.vertical = false;
        this.horizontal = false;
        this.universal = Map.of();
        this.concrete = Map.of();
        return self();
    }
// public# /*FOLD00*/

// #abstract:
    abstract public String display();
    abstract T self();
// abstract#

// #protected: /*FOLD00*/
    // Returns string repr of point
    protected String get(IntPair p) {
        return get(p.x, p.y);
    }

    // Returns string repr of point
    protected String get(int x, int y) {
        String temp;
        if ((temp = concrete.get(new IntPair(x, y))) != null) {
            return temp;
        }
        if ((temp = universal.get(field.get(x, y))) != null) {
            return temp;
        }
        return field.get(x, y).toString();
    }

    // Formats string to spec width
    protected String setWidth(String string, int width) {
        return String.format(String.format(WIDTH_FORMAT, width), string);
    }

    // Creates list from values of iterable
    protected List<String> iterableToString(Iterable<?> values, boolean reverse) {
        ArrayList<String> result = new ArrayList<>();
        for (Object i : values) {
            result.add(i.toString());
        }
        if (reverse) {
            Collections.reverse(result);
        }
        return result;
    }

    protected boolean getHorizontal() {
        return horizontal;
    }

    protected boolean getVertical() {
        return vertical;
    }

    protected List<String> createAxisX() {
        return iterableToString(field.getRangeX(), getHorizontal());
    }

    protected List<String> createAxisY() {
        return iterableToString(field.getRangeY(), getVertical());
    }

    // Returns length of obj.toString()
    protected int getWidth(Object obj) {
        return Objects.toString(obj).length();
    }

    // Returns minima width required along strings
    protected int getMinWidth(List<String> strs) {
        if (strs.size() == 0) {
            return 0;
        }
        int acum = strs.get(0).length();
        for (String str : strs) {
            acum = Math.max(acum, str.length());
        }
        return acum;
    }

    // Translates field's cell into list of strings
    protected List<List<String>> createCells() {
        List<List<String>> result = new ArrayList<>();
        for (int y : field.getRangeY()) {
            result.add(new ArrayList<>());
            for (int x : field.getRangeX()) {
                result.get(y - field.getMinY()).add(get(x, y));
            }
            if (getHorizontal()) {
                Collections.reverse(result.get(y - field.getMinY()));
            }
        }
        if (getVertical()) {
            Collections.reverse(result);
        }
        return result;
    }

    // Sets minima width to all cells
    protected void formatWidth(List<List<String>> cells, int minWidth) {
        for (List<String> row : cells) {
            minWidth = Math.max(minWidth, getMinWidth(row));
        }
        for (List<String> row : cells) {
            for (int i = 0; i < row.size(); ++i) {
                row.set(i, setWidth(row.get(i), minWidth));
            }
        }
    }

    // Returns first cell width
    protected int getCellWidth(List<List<String>> cells) {
        return cells.get(0).get(0).length();
    }

    // Returns field sizes
    protected IntPair getSizes() {
        return new IntPair(field.getRangeX().length(), field.getRangeY().length());
    }

    // Adds prefix along list of strings
    protected List<String> addPrefix(List<String> strings, String prefix) {
        ArrayList<String> result = new ArrayList<>();
        for (String str : strings) {
            result.add(prefix + str);
        }
        return result;
    }

    // Adds suffix along list of strings
    protected List<String> addSuffix(List<String> strings, String suffix) {
        ArrayList<String> result = new ArrayList<>();
        for (String str : strings) {
            result.add(str + suffix);
        }
        return result;
    }

    // Returns diagonal
    protected List<String> getDiagonal(int i, List<? extends List<?>> table) {
        IntPair sz = new IntPair(table.get(0).size(), table.size());
        IntPair point = (i < sz.y)
                        ? new IntPair(0, i)
                        : new IntPair(i - sz.y + 1, sz.y - 1);
        ArrayList<String> diag = new ArrayList<>();
        int x = point.x;
        int y = point.y;
        while (y >= 0 && x < sz.x) {
            diag.add(table.get(y).get(x).toString());
            y -= 1;
            x += 1;
        }
        return diag;
    }

    // Adds axis to cells
    protected void addAxis(List<List<String>> cells) {
        List<String> ax = createAxisX();
        List<String> ay = createAxisY();
        int x = 0;
        for (List<String> row : cells) {
            String t = ay.get(x++);
            row.add(0, t);
            row.add(t);
        }
        ax.add(0, "");
        cells.add(0, ax);
        cells.add(ax);
    }
// protected# /*FOLD00*/
}