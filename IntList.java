package soup.util;

import java.util.Arrays;
import soup.util.Range;

/**
 * Simple dynamic list for primitive type int
 */
public class IntList implements Cloneable {
    // Fields

    // Max capacity of IntList
    public final static int MAX_CAPACITY = 1 << 31 - 8;

    // Storage for elements
    private int[] data;

    // logical size of IntList
    private int size;

    // Constructors

    /**
     * Constructs new empty IntList
     */
    public IntList() {
        data = new int[0];
    }

    /**
     * Constructs new IntList from another IntList
     */
    public IntList(IntList other) {
        data = Arrays.copyOf(data, data.length);
        size = other.size;
    }

    /**
     * Constructs IntList from int values
     */
    public IntList(int... values) {
        this();
        extend(values);
    }

    /**
     * Constructs IntList from Iterable<Integer>
     */
    public IntList(Iterable<Integer> values) {
        this();
        extend(values);
    }

    // Methods

    /**
     * Determines that IntList is equal to some object
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof IntList) {
            IntList o = (IntList) other;
            if (size != o.size) {
                return false;
            }
            for (int i = 0; i < size; i++) {
                if (data[i] != o.data[i]) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Computes hashcode
     */
    @Override
    public int hashCode() {
        int hc = size;
        for (int i = 0; i < size; i++) {
            hc = hc * 1123 + data[size];
        }
        return hc;
    }

    /**
     * Reserves capacity
     */
    public IntList reserve(int capacity) {
        if (data.length < capacity) {
            data = Arrays.copyOf(data, capacity);
        }
        return this;
    }

    /**
     * Deletes everything in range(start, end)
     */
    public IntList clear(int start, int end) {
        start = handleIndex(start);
        if (end < 0) {
            end += size;
        }
        end = Math.min(size, end);
        if (start < end) {
            shift(end, start, size - end);
        }
        size -= end - start;
        return this;
    }

    public IntList clear() {
        size = 0;
        data = new int[0];
        return this;
    }

    /**
     * Removes element from a list. The returning value is whether the element existed.
     */
    public boolean remove(int value) {
        Integer id = index(value);
        if (null == id) {
            return false;
        }
        pop(id.intValue());
        return true;
    }

    /**
     * Returns current size
     */
    public int size() {
        return size;
    }

    /**
     * Returns current capacity
     */
    public int capacity() {
        return data.length;
    }

    /**
     * Reverses this list
     */
    public IntList reverse() {
        for (int i = 0; i < size/2; i++) {
            int exc = data[i];
            data[i] = data[size - i - 1];
            data[size - i - 1] = exc;
        }
        return this;
    }

    /**
     * Sorts the list
     */
    public IntList sort() {
        Arrays.sort(data, 0, size);
        return this;
    }

    /**
     * Counts number of occurences of value
     */
    public int count(int value) {
        int cnt = 0;
        for (int id = 0; id < size; id++) {
            if (data[id] == value) {
                cnt++;
            }
        }
        return cnt;
    }

    /**
     * Returns index of a value if value exists, otherwise null
     */
    public Integer index(int value, int start, int end) {
        start = handleIndex(start);
        if (end < 0) {
            end += size;
        }
        end = Math.min(size, end);
        for (int i = start; i < end; i++) {
            if (data[i] == value) {
                return i;
            }
        }
        return null;
    }

    public Integer index(int value, int start) {
        return index(value, start, size);
    }

    public Integer index(int value) {
        return index(value, 0, size);
    }

    /**
     * Creates copy of this IntList
     */
    @Override
    public IntList clone() {
        return new IntList(this);
    }

    /**
     * Inserts element, if id > size IndexOutOfBoundsException is thrown
     */
    public void insert(int id, int value) {
        size++;
        id = handleIndex(id);
        shift(id, id + 1, size - id - 1);
        data[id] = value;
    }

    /**
     * Swaps two elements in list
     */
    public void swap(int i, int j) {
        i = handleIndex(i);
        j = handleIndex(j);
        data[i] = data[i] ^ data[j] ^ (data[j] = data[i]);
    }

    /**
     * Checks whether IntList is empty
     */
    public boolean empty() {
        return size == 0;
    }

    /**
     * Creates an int array from content of this IntList.
     */
    public int[] toIntArray() {
        return Arrays.copyOf(data, size);
    }

    /**
     * Returns string representation of IntList
     */
    public String toString(String sep) {
        int iMax = size - 1;
        if (iMax == -1)
            return "[]";
        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; i < size; i++) {
            b.append(data[i]);
            if (i == iMax) {
                break;
            }
            b.append(sep);
        }
        return b.append(']').toString();
    }

    @Override
    public String toString() {
        return toString(", ");
    }

    /**
     * Resizes IntList. If new size is bigger than previous the end
     * is filled with zeroes.
     */
    public IntList resize(int size) {
        manageCapacity(size);
        Arrays.fill(data, size, data.length, 0);
        this.size = size;
        return this;
    }

    /**
     * Adds value to the end.
     */
    public void append(int value) {
        manageCapacity(size + 1);
        data[size++] = value;
    }

    /**
     * Adds many values to the end.
     */
    public void extend(int... values) {
        int n = values.length;
        manageCapacity(size + n);
        System.arraycopy(values, 0, data, size, n);
        size += n;
    }

    public void extend(Iterable<Integer> values) {
        for (int i : values) {
            append(i);
        }
    }

    /**
     * Sets capacity equal to the size.
     */
    public IntList shrinkToFit() {
        data = Arrays.copyOf(data, size);
        return this;
    }

    /**
     * Returns the element at position id.
     */
    public int get(int id) {
        id = handleIndex(id);
        return data[id];
    }

    /**
     * Sets element to the position id.
     */
    public void set(int id, int value) {
        id = handleIndex(id);
        data[id] = value;
    }

    /**
     * Removes and returns value from position id, without argument
     * removes and returns last element.
     */
    public int pop(int id) {
        if (empty()) {
            throw new IllegalStateException("Cannot pop value from empty list");
        }
        id = handleIndex(id);
        int val = data[id];
        shift(id + 1, id, --size - id);
        return val;
    }

    public int pop() {
        return pop(-1);
    }

    // Shifts elements
    protected void shift(int from, int to, int cnt) {
        System.arraycopy(data, from, data, to, cnt);
    }

    // Checks whether index lies in range
    // and converts it from negative to positive values.
    protected int handleIndex(int id) {
        Range idRange = new Range(-size, size);
        if (!idRange.contains(id)) {
            throw new IndexOutOfBoundsException(
                String.format("Invalid index %d for list with size %d", id, size)
            );
        }
        if (id < 0) {
            id += size;
        }
        return id;
    }

    // Checks whether more capacity needed for this size
    // or less. If size < 1/4 capacity, then new capacity
    // will be equal to ceil(log2(size)) * 2.
    protected void manageCapacity(int size) {
        if (size > MAX_CAPACITY) {
            throw new IndexOutOfBoundsException("Requested size is bigger than list's capacity limit");
        }
        int clog0 = 32 - Integer.numberOfLeadingZeros(size - 1);
        int flog1 = 31 - Integer.numberOfLeadingZeros(data.length);
        if ((clog0 >= 30) && (data.length < MAX_CAPACITY)) {
            data = Arrays.copyOf(data, MAX_CAPACITY);
        } else if (clog0 + 1 != flog1) {
            data = Arrays.copyOf(data, 1 << (clog0 + 1));
        }
    }
}