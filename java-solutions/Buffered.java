import java.io.Reader;
import java.io.IOException;

public class Buffered extends StorageReader {

    // Value before first read()
    public static final int NOT_INIT = -2;

    // Value indicating end reached
    public static final int END_OF_INPUT = -1;

    // Default source size
    public static final int DEFAULT_BUFFER_SIZE = 1024;

    // Last character read
    public int ch = NOT_INIT;

    public Buffered(Reader source, int bsize) {
        super.source = source;
        //System.out.println(source);
        data = new char[bsize];
    }

    public Buffered(Reader source) {
        this(source, DEFAULT_BUFFER_SIZE);
    }

    // If source not ready, reads. Otherwise it does nothing
    public void prepare() throws IOException {
        if (ch == NOT_INIT) {
            read();
        }
    }

    // last read char
    public char lastch() {
        return (char) ch;
    }

    @Override
    public boolean more() throws IOException {
        prepare();
        return (boundary != END_OF_INPUT);
    }

    @Override
    public int read() throws IOException {
        if (pos >= boundary) {
            boundary = source.read(data);
            pos = 0;
        }
        if (boundary > 0) {
            ch = data[pos++];
        } else {
            ch = -1;
        }
        return ch;
    }
}
