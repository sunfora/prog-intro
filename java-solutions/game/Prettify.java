package game;

import java.util.*;

public class Prettify {
// #fields: /*FOLD00*/
    private final Field field;
    private Map<IntPair, String> concrete;
    private Map<Cell, String> universal;
    private String form;
    private String sep;
    private boolean axis;
    private int width;
    private static final String WIDTH_FORMAT = "%%%ds";
// fields# /*FOLD00*/

// #constructors: /*fold00*/
    public Prettify(Field field) {
        this.field = Objects.requireNonNull(field);
        this.concrete = Map.of();
        this.universal = Map.of();
    }
// constructors# /*FOLD00*/

// #public: /*FOLD00*/

    public void grid() {
        throw new IllegalArgumentException("Grid takes at least 1 argument got 0.");
    }

    // Sets grid and maybe separators and other things
    // first argument has to be one of these "orthogonal"/"ort"/"hexagonal"/"hex";
    // second argument is a separator for ort grid
    public Prettify grid(String... args) { /*FOLD01*/
        Objects.requireNonNull(args);
        this.form = args[0];
        switch (form) {
            case "orthogonal": case "ort":
                if (args.length > 2) {
                    throw new IllegalArgumentException(
                        "Too many arguments for orthogonal grid: expected 1, got " + args.length + "."
                    );
                } else if (args.length == 1) {
                    this.sep = " ";
                } else {
                    this.sep = args[1];
                }
                break;
            case "hexagonal": case "hex":
                break;
            default:
                throw new IllegalArgumentException("Not a valid argument fro grid form");
        }
        return this;
    } /*FOLD01*/

    public Prettify showAxis() {
        return showAxis(true);
    }

    public Prettify showAxis(boolean value) {
        this.axis = value;
        return this;
    }

    public Prettify displayUniversal(Map<Cell, String> map) {
        this.universal = Map.copyOf(map);
        return this;
    }

    public Prettify displayConcrete(Map<IntPair, String> map) {
        this.concrete = Map.copyOf(map);
        return this;
    }

    public Prettify setWidth(int width) {
        assert width >= 0 : "negative width";
        this.width = width;
        return this;
    }

    public String toString() {
        switch (form) {
            case "orthogonal": case "ort":
                return drawOnOrtGrid(sep);
            case "hexagonal": case "hex":
                return drawOnHexGrid();
        }
        throw new IllegalStateException("Prettify is unnabe to draw that type");
    }
// public# /*FOLD00*/

// #utils: /*FOLD00*/
    private int max() {
        throw new IllegalArgumentException("max 0 arguments");
    }

    private int max(int... nums) {
        int acum = Integer.MIN_VALUE;
        for (int i = 0; i < nums.length; ++i) {
            acum = Math.max(acum, nums[i]);
        }
        return acum;
    }

    private <T> T swap(T a, T b) {
        return a;
    }

    private int cdiv(int a, int b) {
        return (a + b - 1)/b;
    }
// utils# /*fold00*/

// #private: /*FOLD00*/

    // Returns string repr of point
    private String get(IntPair p) {
        return get(p.x, p.y);
    }

    // Returns string repr of point
    private String get(int x, int y) { /*FOLD01*/
        String temp;
        if ((temp = concrete.get(new IntPair(x, y))) != null) {
            return temp;
        }
        if ((temp = universal.get(field.get(x, y))) != null) {
            return temp;
        }
        return field.get(x, y).toString();
    } /*FOLD01*/

    // Formats string to spec width
    private String setWidth(String string, int width) {
        return String.format(String.format(WIDTH_FORMAT, width), string);
    }

    // Creates String of axis values
    private List<String> createAxis(int begin, int end) { /*fold01*/
        ArrayList<String> ax = new ArrayList<>();
        for (int i = begin; i < end; ++i) {
            ax.add(Integer.toString(i));
        }
        return ax;
    } /*FOLD01*/

    // Does the same but using ranges
    private List<String> createAxis(Range range) {
        return createAxis(range.inf, range.sup);
    }

    // Returns length of obj.toString()
    private int getWidth(Object obj) {
        return obj.toString().length();
    }

    // Returns minima width required along strings
    private int getMinWidth(List<String> strs) { /*fold01*/
        if (strs.size() == 0) {
            return 0;
        }
        int acum = strs.get(0).length();
        for (String str : strs) {
            acum = Math.max(acum, str.length());
        }
        return acum;
    } /*FOLD01*/

    // Tranlates field's cell into list of strings
    private List<List<String>> createCells() { /*FOLD01*/
        List<List<String>> result = new ArrayList<>();
        for (int y : field.getRangeY()) {
            result.add(new ArrayList<>());
            for (int x : field.getRangeX()) {
                result.get(y).add(get(x, y));
            }
        }
        return result;
    } /*FOLD01*/

    // Sets minima width to all cells
    private void formatWidth(List<List<String>> cells, int minWidth) { /*FOLD01*/
        for (List<String> row : cells) {
            minWidth = max(minWidth, getMinWidth(row));
        }
        for (List<String> row : cells) {
            for (int i = 0; i < row.size(); ++i) {
                row.set(i, setWidth(row.get(i), minWidth));
            }
        }
    } /*FOLD01*/

    // Creates axis for hex using two standrard
    private List<String> createHexAx(List<String> axFirst, List<String> axSecond) { /*fold01*/
        ArrayList<String> result = new ArrayList<>(axFirst);
        result.add("");
        result.add("");
        result.addAll(axSecond);
        return result;
    } /*FOLD01*/

    // Adds prefix along list of strings                                            2
    private List<String> addPrefix(List<String> strings, String prefix) { /*fold01*/
        ArrayList<String> result = new ArrayList<>();
        for (String str : strings) {
            result.add(prefix + str);
        }
        return result;
    } /*FOLD01*/

    // Creates hex grid, like this:
    /*
     \_/ \_/ \_/ \ type0
     / \_/ \_/ \_/ type1
     \_/ \_/ \_/ \ type0
     / \_/ \_/ \_/ type1
     \_/ \_/ \_/ \ type0

     first = 1 swaps type0, type1
     width sets width of grid
     height sets number of rows
     t is width of cell
     */
    private List<String> createHexGrid(int first, int width, int height, int t) { /*FOLD01*/
        List<String> grid = new ArrayList<>();
        String type0 = "\\" + "_".repeat(t) + "/" + " ".repeat(t);
        String type1 = "/" + " ".repeat(t) + "\\" + "_".repeat(t);
        if (first == 1) {
            type0 = swap(type1, type1 = type0);
        }
        type0 = type0.repeat(cdiv(width, type0.length())).substring(0, width);
        type1 = type1.repeat(cdiv(width, type1.length())).substring(0, width);
        for (int i : new Range(0, height)) {
            grid.add(((i&1) == 1)? type1 : type0);
        }
        return grid;
    } /*FOLD01*/

    // Returns diagonal as an Array
    private Object[] getDiagonal(int i, List<List<String>> table) { /*FOLD01*/
        IntPair size = new IntPair(table.get(0).size(), table.size());
        IntPair point = i < size.y? new IntPair(0, i) : new IntPair(i - size.y + 1, size.y - 1);
        ArrayList<String> diag = new ArrayList<>();
        int x = point.x;
        int y = point.y;
        while (y >= 0 && x < size.x) {
            diag.add(table.get(y).get(x));
            y -= 1;
            x += 1;
        }
        return diag.toArray();
    } /*FOLD01*/


    // Determines how much image must be shifted
    // for axis to be displayed
    private int getRightShift(int t, int a, List<String> axLeft) { /*fold01*/
        int d = 0;
        int i = 0;
        for (String anno: axLeft) {
            int left = getShift(a, i, t);
            d = max(d, max(left, anno.length()) - left);
            i += 1;
        }
        return d;
    } /*FOLD01*/

    // Returns how much concrete line must be shifted (or cut in front/behind)
    // in order to be diplayed correctly
    private int getShift(int a, int i, int t) {
        return max((Math.abs(a - i) - (i > a? 1 : 0))*(t + 1) - t, 0);
    }

    // Set's up format to grid cells
    // %{n}s cells %s, where %s - for axis values
    // every cell now is something like /%{t}s\ instead of spaces
    private String setUpFormat(String hexCells, int left, int right, int t, int d) { /*FOLD01*/
        hexCells = hexCells.substring(left, right);
        String tSp = " ".repeat(t);
        String rep = "%%" + t + "s";
        int a = hexCells.startsWith(tSp)? t : 0;
        int b = hexCells.endsWith(tSp)? hexCells.length() - t : hexCells.length();
        if (a <= b) {
            hexCells = hexCells.substring(0, a)
                       + hexCells.substring(a, b).replaceAll(tSp, rep)
                       + hexCells.substring(b, hexCells.length());
        }

        if (left + d != 0) {
            return "%" + (left + d) + "s" + hexCells + "%s";
        } else {
            return "%s" + hexCells + "%s";
        }
    } /*FOLD01*/

    private String drawOnHexGrid() { /*FOLD01*/
        List<List<String>> cells = createCells();
        int a = field.getRangeX().length();
        int b = field.getRangeY().length();
        if (a == 0 || b == 0) {
            return "";
        }
        formatWidth(cells, this.width);
        int t = cells.get(0).get(0).length();
        // Bounding box
        int width = (a + b - 1)*(t+1) + 1;
        int height = a + b + 2;
        // Create axis for hex grid
        List<String> leftAx;
        List<String> rightAx;
        if (axis) {
            leftAx = createHexAx(
                addPrefix(createAxis(field.getRangeX()), "R"),
                addPrefix(createAxis(field.getRangeY()), "B")
            );
            rightAx = createHexAx(
                addPrefix(createAxis(field.getRangeY()), "B"),
                addPrefix(createAxis(field.getRangeX()), "R")
            );
        } else {
            leftAx = new ArrayList<>(Collections.nCopies(height, ""));
            rightAx = leftAx;
        }
        int d = getRightShift(t, a, leftAx);
        List<String> grid = createHexGrid(1 - (a&1), width, height, t);
        // Drawing process
        for (int i : new Range(0, height)) {
            int left = getShift(a, i, t);
            int right = width - getShift(b, i, t);
            grid.set(i, setUpFormat(grid.get(i), left, right, t, d));
            grid.set(i, String.format(grid.get(i), leftAx.get(i), rightAx.get(i)));
            if (i != 0 && i != height - 1) {
                grid.set(i, String.format(grid.get(i), getDiagonal(i - 1, cells)));
            }
        }
        StringBuilder result = new StringBuilder();
        for (String row: grid) {
            result.append(row);
            result.append(System.lineSeparator());
        }
        result.setLength(result.length() - System.lineSeparator().length());
        return result.toString();
    } /*FOLD01*/

    // Adds axis to cells
    private void addAxis(List<List<String>> cells) { /*FOLD01*/
        List<String> ax = createAxis(field.getRangeX());
        List<String> ay = createAxis(field.getRangeY());
        int x = 0;
        for (List<String> row : cells) {
            String t = ay.get(x++);
            row.add(0, t);
            row.add(t);
        }
        ax.add(0, "");
        cells.add(0, ax);
        cells.add(ax);
    } /*FOLD01*/

    private String drawOnOrtGrid(String sep) { /*FOLD01*/
        List<List<String>> cells = createCells();
        int a = field.getRangeX().length();
        int b = field.getRangeY().length();
        if (a == 0 || b == 0) {
            return "";
        }
        if (axis) {
            addAxis(cells);
        }
        formatWidth(cells, this.width);
        StringBuilder table = new StringBuilder();
        for (List<String> row : cells) {
            for (String str : row) {
                table.append(str);
                table.append(sep);
            }
            table.replace(table.length() - 1, table.length(), System.lineSeparator());
        }
        table.setLength(table.length() - System.lineSeparator().length());
        return table.toString();
    } /*FOLD01*/

    private String drawOnOrtGrid() {
        return drawOnOrtGrid(" ");
    }
// private# /*fold00*/
}