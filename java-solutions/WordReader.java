import java.util.*;
import java.io.*;

class WordReader implements Closeable {

    private static int defaultBufferSize = 1024;
    private char[] word;
    private int wordsize;
    private BufferedReader source;
    private boolean closed;

    public WordReader(BufferedReader source) {
        this.source = Objects.requireNonNull(source);
        word = new char[1024];
    }

    public static boolean isWordChar(char c) {
        int type = Character.getType(c);
        boolean isDash = (type == Character.DASH_PUNCTUATION);
        boolean isLetter = (Character.isLetter(c));
        boolean isApo = (c == '\'');
        return (isLetter || isDash || isApo);
    }

    public boolean isClosed() {
        return closed;
    }

    public boolean hasNextWord() throws IOException {
        checkNotClosed();
        if (wordsize == 0) {
            tryReadWord();
        }
        return (wordsize != 0);
    }

    public String nextWord() throws IOException {
        checkNotClosed();
        if (!hasNextWord()) {
            throw new NoSuchElementException("No word available");
        }
        String result = new String(word, 0, wordsize);
        wordsize = 0;
        return result;
    }

    @Override
    public void close() throws IOException {
        if (null != source) {
            try {
                source.close();
            } finally {
                source = null;
                word = null;
                closed = true;
            }
        }
    }

    private void checkNotClosed() throws IOException {
        if (closed) {
            throw new IOException("WordReader Closed");
        }
    }

    private void tryReadWord() throws IOException {
        int chr = source.read();
        while (chr != -1) {
            boolean isWord = isWordChar((char) chr);
            if (wordsize == 0 && !isWord) {
                chr = source.read();
                continue;
            } else if (isWord) {
                if (wordsize >= word.length) {
                    word = Arrays.copyOf(word, word.length * 2);
                }
                word[wordsize++] = (char) chr;
                chr = source.read();
            } else {
                return;
            }
        }
    }
}