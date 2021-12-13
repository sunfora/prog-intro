package md2html;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sorted {
    private Sorted() {}

    public static <E extends Comparable<E>> List<E> list(Collection<E> collection) {
        List<E> result = new ArrayList<>(collection);
        Collections.sort(result);
        return result;
    }
}