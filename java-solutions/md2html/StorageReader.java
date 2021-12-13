package md2html;

import java.io.Reader;
import java.io.IOException;
import java.util.Arrays;

public abstract class StorageReader extends Reader {
    // Wrapped reader
    protected Reader source;

    // Storage
    protected char[] data;

    // Current position in storage
    protected int pos;

    // Actual boundary of the storage
    protected int boundary;

    // Should tell whether more input available
    abstract public boolean more() throws IOException;

    // Sets storage's capacity
    public void setCapacity(int capacity) {
        data = Arrays.copyOf(data, capacity);
    }

    // Returns storage's capacity
    public int getCapacity() {
        return data.length;
    }

    // Tells current length of saved data
    public int length() {
        return boundary;
    }

    // Doubles capacity of storage
    public void doubleCapacity() {
        setCapacity(data.length * 2);
    }

    // Resets storage
    public void resetStorage() {
        pos = 0;
        boundary = 0;
    }

    // Shifts all elements in storage to the left
    public void shiftStorage(int offset) {
        System.arraycopy(data, offset, data, 0, boundary - offset);
        boundary -= offset;
        pos = boundary;
    }

    // Returns one char from data in current pos
    public char get(int pos) throws IOException {
        checkBounds(pos);
        return data[pos];
    }

    public void set(char val, int pos) {
        checkBounds(pos);
        data[pos] = val;
    }

    @Override
    public int read(char[] data, int offset, int cnt) throws IOException {
        return source.read(data, offset, cnt);
    }

    @Override
    public int read() throws IOException {
        return source.read();
    }

    @Override
    public void close() throws IOException {
        if (source != null) {
            try {
                source.close();
            } finally {
                data = null;
                source = null;
            }
        }
    }

    protected void checkBounds(int pos) {
        if (pos >= boundary || pos < 0) {
            throw new IndexOutOfBoundsException("index: " + pos + ", length: " + boundary);
        }
    }
}