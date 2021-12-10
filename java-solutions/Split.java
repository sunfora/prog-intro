import java.util.Arrays;
import java.io.Reader;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.ConcurrentModificationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.Closeable;


/**
 * Split is a tool to iterate over input as it have been splitted string.
 * Split contains many views, the default (0 view) is just all input.
 * Every view can be splitted, so new View will be created. However
 * calling split method again will restrict usage of all previous views.
 */
public class Split implements Closeable {
    // Fields /*fold00*/

    // Views
    private ArrayList<View> levels = new ArrayList<View>();

    // Initial view after Split's creation
    private final View zero = new View();

    // Cached reader
    private Cached cache;

    // How many characters shifted (negative value)
    private int shifted = 0;

    // Flag indicating Split is closed
    private boolean closed;

    // Constructors /*fold00*/

    /**
     * Constructs new Split entity from a reader.
     */
    public Split(Reader source) { /*fold01*/
        cache = new Cached(source);
    } /*fold01*/

    /**
     * Constructs new Split entity with views created from delimiters.
     */
    public Split(Reader source, Matcher... delimiters) { /*fold01*/
        this(source);
        into(delimiters);
    } /*fold01*/

    // Methods /*fold00*/

    /**
     * Returns a view by its id.
     * If id < 0 then last created view returned.
     */
    public View view(int id) { /*fold01*/
        ensureNotClosed();
        Range idRange = new Range(-levels.size(), levels.size());
        if (!idRange.contains(id)) {
            throw new IndexOutOfBoundsException(
                id + " is invalid index of level for Split with " + levels.size() + " view levels"
            );
        }
        if (id < 0) {
            id += levels.size();
        }
        return levels.get(id);
    } /*fold01*/

    /**
     * Returns list view on all views
     */
    public List<View> views() { /*fold01*/
        return Collections.unmodifiableList(levels);
    } /*fold01*/

    /**
     * Splits into views .
     */
    public List<View> into(Matcher... delimiters) { /*fold01*/
        ensureNotClosed();
        View level = zero;
        for (Matcher delimiter : delimiters) {
            level = level.split(delimiter);
        }
        return views();
    } /*fold01*/

    /**
     * Returns current Split depth.
     */
    public int depth() { /*fold01*/
        ensureNotClosed();
        return levels.size();
    } /*fold01*/

    /**
     * Closes Split
     */
    @Override
    public void close() throws IOException { /*fold01*/
        if (!closed) {
            zero.restrictUsageOfDescendants();
            zero.getRestriction(this);
            levels.clear();
            closed = true;
            try {
                cache.close();
            } finally {
                cache = null;
            }
        }
    } /*fold01*/

    // Shifts cache, updates state of all views, and returns provided String
    private String shiftUpdateReturnToken(int caller, Range tokenRange, String token) { /*fold01*/
        ensureNotClosed();
        cache.shiftStorage(tokenRange.sup);
        int offset = zero.offset;
        for (View level : levels) {
            level.offset = offset;
            // Update delRange and offset
            Range del = level.delRange;
            level.delRange = new Range(null);
            level.tokenRange = new Range(null);
            if (!del.empty) {
                Range shifted = new Range(
                    Math.max(offset, del.inf - tokenRange.sup),
                    Math.max(offset, del.sup - tokenRange.sup)
                );
                if (shifted.inf == offset && level.id >= caller) {
                    level.offset += shifted.length();
                } else {
                    level.delRange = shifted;
                }
            }
            // Update tokenRange
            if (!level.delRange.empty) {
                level.tokenRange = new Range(level.offset, level.delRange.inf);
            }
            offset = level.offset;
            level.pos = Math.max(offset, level.pos - tokenRange.sup);
        }
        return token;
    } /*fold01*/

    // Makes usage of Split impossible after close
    private void ensureNotClosed() throws IllegalStateException { /*fold01*/
        if (closed) {
            throw new IllegalStateException("Split is closed");
        }
    } /*fold01*/

    // Nested Classes /*fold00*/
    /* View is Node-like class that is up to iterating over input.
     * It may create new nodes, but now can have only one child and only one parent
     */
    public class View { /*fold01*/
        // Fields /*fold02*/
        private final int id;

        private boolean usageRestricted;
        private Object restrictionFrom;

        private final View parent;
        private View child;

        private final Matcher delimiter;

        private Range tokenRange = new Range(null);
        private Range delRange = new Range(null);

        private int offset;
        private boolean locked;
        private int pos;

        // Constructors /*fold02*/

        // Constructs view from parent and delimiter
        private View(Matcher delimiter, View parent) { /*fold03*/
            this.delimiter = delimiter.clone();
            this.parent = parent;
            offset = parent.offset;
            pos = offset;
            id = parent.id + 1;
            levels.add(this);
        } /*fold03*/

        // Constructs default (zero) view
        private View() { /*fold03*/
            id = 0;
            offset = 0;
            delimiter = null;
            parent = null;
            levels.add(this);
        } /*fold03*/

        // Methods /*fold02*/

        /**
         * Tells whether a next token available
         */
        public boolean hasNext() throws IOException { /*fold03*/
            ensureNotRestricted();
            return !(locked && tokenRange.empty);
        } /*fold03*/

        /**
         * Returns next found token
         */
        public String next() throws IOException { /*fold03*/
            ensureNotRestricted();
            unlockDescendants();
            return shiftUpdateReturnToken(id, collectToken(), showToken());
        } /*fold03*/

        /**
         * Returns last found token without moving onto the next position.
         */
        public String showToken() throws IOException { /*fold03*/
            ensureNotRestricted();
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements for this view");
            }
            return cache.extract(collectToken());
        } /*fold03*/

        // Getters /*fold03*/

        /**
         * Returns parent of this view
         */
        public View getParent() {
            return parent;
        }

        /**
         * Returns child of this view
         */
        public View getChild() {
            return child;
        }

        /**
         * Returns this view id
         */
        public int getId() {
            return id;
        }
        // /*fold03*/

        /**
         * Creates a new view in the scope of vision of this view with
         * specified delimiter.
         */
        public View split(Matcher delimiter) { /*fold03*/
            ensureNotRestricted();
            Objects.requireNonNull(delimiter);
            restrictUsageOfDescendants();
            child = new View(delimiter, this);
            return child;
        } /*fold03*/

        // Method called by child to detect whether position is in token of
        // a parent
        private boolean liesInToken(int pos) throws IOException { /*fold03*/	   
            if (id == 0) {
                return ((cache.length() > pos) || cache.more());
            }
            if (pos < this.pos && delRange.empty) {
                return true;
            } else if (delRange.empty) {
                moveWhileSubset();
            }
            if (!tokenRange.empty) {
                return tokenRange.contains(pos);
            }
            return pos < this.pos;
        } /*fold03*/

        // Moves position while delimiter can be found
        // If delimiter already was found, nothing happens
        private void moveWhileSubset() throws IOException { /*fold03*/
            Range previous = delRange;
	    boolean done = !delRange.empty;
	    while (!done && parent.liesInToken(pos)) {
                done = true;
		if (delimiter.found()) {
		    delRange = new Range(pos - delimiter.matchSize(), pos);
		    previous = delRange;
		}
                delimiter.send(cache.get(pos++));
                Range current = new Range(pos - delimiter.matchSize(), pos);
                if (current.isSupersetOf(previous)) {
                    done = false;
                    if (delimiter.found()) {
		    	delRange = new Range(pos - delimiter.matchSize(), pos);
		    }
		    previous = current;
                }
            }
	    if (delimiter.found() && delRange.empty) {
	    	delRange = new Range(pos - delimiter.matchSize(), pos);
	    }
            // Update tokenRange
            if (!delRange.empty || !parent.liesInToken(pos)) {
                delRange = (delRange.empty)? new Range(pos, pos) : delRange;
                tokenRange = new Range(offset, delRange.inf);
                if (!parent.liesInToken(delRange.sup)) {
                    locked = true;
                }
            }
        } /*fold03*/

        // Collects token
        private Range collectToken() throws IOException { /*fold03*/
            if (id == 0) {
                while (cache.more()) {
                    cache.cache(1024);
                }
                tokenRange = new Range(0, cache.length());
                locked = true;
            }
            while (tokenRange.empty) {
                moveWhileSubset();
            }
            return tokenRange;
        } /*fold03*/

        // Restricts usage of View after parent's new split
        private void ensureNotRestricted() throws ConcurrentModificationException { /*fold03*/
            if (usageRestricted) {
                throw new ConcurrentModificationException(
                    "Usage restricted by " + restrictionFrom.toString() + " after"
                );
            }
        } /*fold03*/

        // Sets restriction for every node below
        private void restrictUsageOfDescendants() { /*fold03*/
            View child = this.child;
            levels.subList(id + 1, levels.size()).clear();
            while (null != child) {
                child.getRestriction(this);
                child = child.child;
            }
        } /*fold03*/

        private void getRestriction(Object from) { /*fold03*/
            usageRestricted = true;
            restrictionFrom = from;
        } /*fold03*/

        // Resets and unlocks locked descendants after the next method
        private void unlockDescendants() { /*fold03*/
            View child = this.child;
            while (null != child) {
                child.locked = false;
                child.delimiter.reset();
                child = child.child;
            }
        } /*fold03*/

    } /*fold01*/
} /*fold00*/
