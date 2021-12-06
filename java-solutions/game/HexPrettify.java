package game;

import java.util.*;

public class HexPrettify extends AbstractPrettify<HexPrettify> {
// #constructors:
    public HexPrettify(Field<?> field) {
        super(field);
    }

    public HexPrettify() {
        super();
    }
// constructors#

// #implemented:
    public String display() {
        List<List<String>> cells = createCells();
        IntPair sz = getSizes();
        if (sz.x == 0 || sz.y == 0) {
            return "";
        }
        formatWidth(cells, this.width);
        int cWidth = getCellWidth(cells);
        // Grid sizes
        int width = (sz.x + sz.y - 1) * (cWidth + 1) + 1;
        int height = sz.x + sz.y + 2;
        // Create axis for hex grid and grid itself
        Pair<List<String>, List<String>> ax = new Pair<>(new ArrayList<>(), new ArrayList<>());
        fillAxForHexGrid(ax.first, ax.second, height);
        List<String> grid = createHexGrid(sz.x & 1, width, height, cWidth);
        formatGrid(grid, cells, sz, ax);
        return String.join(System.lineSeparator(), grid);
    }

    @Override
    protected boolean getHorizontal() {
        return !super.getHorizontal();
    }

    @Override
    protected boolean getVertical() {
        return !super.getVertical();
    }

    @Override
    public HexPrettify setToDefault() {
        super.setToDefault();
        this.width = 2;
        return self;
    }
// implemented#

// #protected:
    protected void formatGrid(
        List<String> grid,
        List<List<String>> cells,
        IntPair sz,
        Pair<List<String>, List<String>> ax
    ) {
        int width = grid.get(0).length();
        int height = grid.size();
        int cWidth = getCellWidth(cells);
        int d = getRightShift(cWidth, sz.x, ax.first);
        for (int i = 0; i < height; ++i) {
            // replace grid row by formatted grid row
            int left = getShift(sz.x, i, cWidth);
            int right = width - getShift(sz.y, i, cWidth);
            grid.set(i, setUpFormat(grid.get(i), left, right, cWidth, d));
            // format grid row, by adding left and right axis values
            grid.set(i, String.format(grid.get(i), ax.first.get(i), ax.second.get(i)));
            // place all other values in hex cells
            if (i != 0 && i != height - 1) {
                grid.set(i, String.format(grid.get(i), getDiagonal(i - 1, cells).toArray()));
            }
        }
    }

    // Creates axis for hex using two standard
    protected List<String> createHexAx(List<String> axFirst, List<String> axSecond) {
        ArrayList<String> result = new ArrayList<>(axFirst);
        result.add("");
        result.add("");
        result.addAll(axSecond);
        return result;
    }

    /* Creates hex grid, like this:

     / \_/ \_/ \_/ type0
     \_/ \_/ \_/ \ type1
     / \_/ \_/ \_/ type0
     \_/ \_/ \_/ \ type1
     / \_/ \_/ \_/ type0
     \_/ \_/ \_/ \ type1

     first = 1 swaps type0, type1
     width sets width of grid
     height sets number of rows
     t is width of cell
     */
    protected List<String> createHexGrid(int first, int width, int height, int t) {
        List<String> grid = new ArrayList<>();
        String[] type = new String[] {
            repeat(( "/" + " ".repeat(t) + "\\" + "_".repeat(t)), width),
            repeat(("\\" + "_".repeat(t) +  "/" + " ".repeat(t)), width)
        };
        if (first == 1) {
            type[0] = Util.swap(type[1], type[1] = type[0]);
        }
        for (int i : new Range(0, height)) {
            grid.add(type[i&1]);
        }
        return grid;
    }

    // Resizes string to width
    protected String repeat(String str, int width) {
        int times = Util.cdiv(width, str.length());
        return str.repeat(times).substring(0, width);
    }

    // Determines how much image must be shifted
    // for axis to be displayed
    protected int getRightShift(int t, int a, List<String> axLeft) {
        int d = 0;
        int i = 0;
        for (String anno: axLeft) {
            int left = getShift(a, i, t);
            d = Math.max(d, Math.max(left, anno.length()) - left);
            i += 1;
        }
        return d;
    }

    // Returns how much concrete line must be shifted (or cut in front/behind)
    // in order to be diplayed correctly
    protected int getShift(int a, int i, int t) {
        return Math.max((Math.abs(a - i) - (i > a? 1 : 0))*(t + 1) - t, 0);
    }

    // Set's up format to grid cells
    // %{n}s cells %s, where %s - for axis values
    // every cell now is something like /%{t}s\ instead of spaces
    protected String setUpFormat(String hexCells, int left, int right, int t, int d) {
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
    }

    protected void fillAxForHexGrid(List<String> leftAx, List<String> rightAx, int height) {
        if (axis) {
            leftAx.addAll(createHexAx(
                createAxisX(),
                createAxisY()
            ));
            rightAx.addAll(createHexAx(
                createAxisY(),
                createAxisX()
            ));
        } else {
            IntPair sz = getSizes();
            leftAx.addAll(new ArrayList<>(Collections.nCopies(height, "")));
            rightAx.addAll(leftAx);
        }
    }
// protected#
}