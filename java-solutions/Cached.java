import java.io.IOException;
import java.io.Reader;

// It is mainly a wrapper over Buffer, but also can wrap any other
// Reader.
// If provided reader is not a subclass of Buffer, then
// new Cache(new Reader()) <=> new Cache(new Buffered(new Reader())
public class Cached extends StorageReader {

    Buffered buffer;

    // Default cache size
    public static final int DEFAULT_CACHE_SIZE = 128;
    
    public Cached(Reader source) {
        if (source instanceof Buffered) {
            buffer = (Buffered) source;
        } else {
            buffer = new Buffered(source);
        }
        super.source = buffer;
        data = new char[DEFAULT_CACHE_SIZE];
    }

    @Override
    public boolean more() throws IOException {
        return buffer.more();
    }

    @Override
    public char get(int pos) throws IOException {
        if (pos >= boundary) {
            cache(pos + 1 - boundary);
        }
        checkBounds(pos);
        return data[pos];
    }

    public String extract(Range range) {
        if (range.empty || (range.inf == range.sup)) {
            return "";
        }
        checkBounds(range.sup - 1);
        int offset = range.inf;
        int cnt = range.sup - range.inf;
        return new String(data, offset, cnt);
    }

    public String extract(int start, int end) {
        return extract(new Range(start, end));
    }

    public String extract() {
        return extract(0, boundary);
    }

    // Cache n chars or less if end of input is near
    public int cache(int n) throws IOException {
        while (boundary + n >= data.length) {
            doubleCapacity();
        }
        int i = 0;
        for (; i < n && buffer.more(); buffer.read(), i++) {
            data[i + boundary] = buffer.lastch();
        }
        boundary += i;
        return i;
    }

    public char lastch() {
        return buffer.lastch();
    }

    public void setBufferCap(int n) {
        buffer.setCapacity(n);
    }
}