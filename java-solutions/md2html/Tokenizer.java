package md2html;

import java.io.*;
import java.util.*;

public class Tokenizer implements Closeable {

    private Buffered buffer;
    private boolean closed;
    private boolean uf;
    private boolean asText;

    private static final Token DEFAULT_VALUE = new Token(LexType.NONE);
    private Token token = DEFAULT_VALUE;
    private ArrayList<Token> rest = new ArrayList<>();
    boolean lock = false;
    boolean stop = false;

    public Tokenizer(Reader source) {
        this.buffer = new Buffered(source);
    }

    public boolean hasNext() throws IOException {
        return buffer.more() || lock;
    }

    public Token next() throws IOException {
        if (!hasNext()) {
            throw new NoSuchElementException("No more tokens...");
        }
        return wall(collect());
    }

    public void close() throws IOException {
        if (!closed) {
            try {
                buffer.close();
            } finally {
                buffer = null;
            }
            closed = true;
        }
    }

    private Token wall(Token token) {
        if (token.getType() == LexType.CODE && token.length() == 3) {
            asText = asText == false;
            return token;
        }
        if (asText) {
            return new Token(token.toString());
        }
        return token;
    }

    private Token collect() throws IOException {
        if (rest.size() > 0) {
            return rest.remove(rest.size() - 1);
        }
        while (mutate());
        if ((token.getType() == LexType.CODE) && (token.length() != 3)) {
            for (int i = 0; i < token.length(); ++i) {
                rest.add(new Token('`'));
            }
            unlock(token);
            return collect();
        }
        return unlock(token);
    }

    private Token unlock(Token throwBack) {
        lock = false;
        token = DEFAULT_VALUE;
        return throwBack;
    }

    private boolean mutate() throws IOException {
        if (lock) {
            return false;
        }
        LexType got = LexType.detectType(buffer.lastch());
        // Handle escape characters
        if ((token.getType() == LexType.TEXT) &&
            (token.length() > 0) &&
            (token.charAt(token.length() - 1) == '\\')
        ) {
            switch (buffer.lastch()) {
                case '\\': case '`': case '*': case '_':
                case  '{': case '}': case '[': case ']':
                case  '(': case ')': case '#': case '+':
                case  '-': case '.': case '!':
                    token.setLength(token.length() - 1);
                    got = LexType.TEXT;
            }
        }
        // Check whether the same type was catched
        if (!((token.getType() == LexType.NONE) || (token.getType() == got))) {
            lock = true;
            return false;
        }
        // Create token if type is None
        if (token.getType() == LexType.NONE) {
            token = new Token(got);
        }
        // Handle new lines
        if (token.getType() == LexType.NL) {
            if ((token.length() == 0) && (buffer.lastch() != '\r')) {
                lock = true;
            } else if (token.length() == 1) {
                lock = true;
                if (buffer.lastch() != '\n') {
                    return !lock;
                }
            }
        }
        token.append(buffer.lastch());
        if (buffer.more()) {
            lock = (buffer.read() == -1) || lock;
        }
        return !lock;
    }
}