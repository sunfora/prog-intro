package game;

import java.util.*;

public class TestField {
    public static void main(String[] args) {
        int a, b;
        a = Integer.parseInt(args[0]);
        b = Integer.parseInt(args[1]);
        Field field = new CellField(a, b);
        Prettify pretty = (new Prettify(field)).showAxis().grid("hex");
        pretty.displayUniversal(Map.of(Cell.E, ""));
        pretty.displayConcrete(Map.of(new IntPair(1, 2), ""));
        System.out.println(pretty);
    }
}