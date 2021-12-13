package md2html;

import java.io.*;
import java.util.*;
import java.util.function.*;

public class Parser implements Closeable {

    // Fields

    private Tokenizer lex;
    private Tape tape;
    private HTMLable paragraph;
    private boolean locked;
    private boolean closed;

    private Integer[] lastCode;
    private IntList[] tagStacks;

    // Constructors

    public Parser(Reader source) {
        lex = new Tokenizer(source);
        tape = new Tape();
    }

    // Public

    public boolean hasNext() throws IOException {
        if (paragraph != null) {
            return true;
        }
        do {
            prepare(null);
            paragraph = collectBlock();
        } while (paragraph == null && lex.hasNext());
        return paragraph != null;
    }

    public HTMLable next() throws IOException {
        if (!hasNext()) {
            throw new NoSuchElementException("No more blocks");
        }
        return prepare(paragraph);
    }

    public void close() throws IOException {
        if (!closed) {
            try {
                lex.close();
            } finally {
                lex = null;
                tagStacks = null;
                lastCode = null;
                tape = null;
                paragraph = null;
                closed = true;
            }
        }
    }

    // Private

    private HTMLable prepare(HTMLable throwBack) {
        paragraph = null;
        tape.clear();
        for (IntList t : tagStacks) {
            t.clear();
        }
        Arrays.fill(lastCode, null);
        locked = false;
        return throwBack;
    }

    private boolean update() throws IOException {
        if (!lex.hasNext() || locked) {
            return false;
        }
        tape.add(lex.next());
        switch (type(tape.back())) {
            case NL:
                return handleNL();
        }
        return true;
    }

    private void escapeSpaces(List<Box> source) {
        for (int i = 1; i < source.size() - 1; ++i) {
            switch type(source.get(i)) {
                case UNDERSCORE: case DASH: case STAR:
                    if ((type(source.get(i-1)) == LexType.SPACE)
                        && (type(source.get(i+1)) == LexType.SPACE)) {
                        Token tok = new Token(source.get(i).token.toString());
                        source.set(i, new Box(tok, tree(tok)));
                    }
            }
        }
    }

    private List<ParagraphElement> parse(List<Box> source) {
        escapeSpaces(source);
        Tape stack = new Tape();
        Integer[] lastCode = new Integer[4];
        for (int i = 0; i < source.size(); ++i) {
            switch (type(source.get(i))) {
                case CODE:
                case DASH:
                case STAR:
                case UNDERSCORE:
            }
        }
    }

    private HTMLable parseBlock() throws IOException {
        while(update());
        if (tape.size() == 0) {
            return null;
        }
        int begin = 0;
        int end = tape.size();
        TreeConstructor<HTMLable> constructor = Paragraph::new;
        Tags tags = new Tags("", "");
        Box open = tape.get(0);
        Box close = tape.back();
        // Handles header cases
        if (type(open) == LexType.HASH) {
            if (tape.size() == 1) {
                return new Header(new Text(""), new Tags(open, ""));
            }
            if (type(tape.get(1)) == LexType.SPACE) {
                constructor = Header::new;
                begin += 2;
                if (type(close) == LexType.HASH) {
                    tags = new Tags(open, close);
                    end--;
                } else {
                    tags = new Tags(open, "");
                }
            }
        }
        // Single NL ending case
        if (type(tape.get(end-1)) == LexType.NL) {
            end--;
        }
        return constructor.apply(parse(tape.subList(begin, end))), tags);
    }

    // Checks whether the end of paragraph was reached
    private boolean handleNL() {
        if (tape.size() == 1) {
            tape.pop();
            return true;
        }
        if ((tape.size() > 1) && (type(tape.get(tape.size() - 2)) == LexType.NL)) {
            tape.subList(tape.size() - 2, tape.size()).clear();
            locked = true;
        }
        return (!locked);
    }

    // Shorthands and implementations


    // Check if there is SPACE before last token on tape
    private boolean spaceBefore() {
        return (type(tape.get(tape.size() - 2)) == LexType.SPACE);
    }

    private int len(Box box) {
        return box.token.length();
    }

    private int len(Token token) {
        return token.length();
    }

    private LexType type(Box box) {
        return box.token.getType();
    }

    private LexType type(Token token) {
        return token.getType();
    }

    private ParagraphElement tree(Token token) {
        return new Text(token.toString());
    }

    private ParagraphElement tree(Box box) {
        if (box.tree == null) {
            return (ParagraphElement) tree(box.token);
        }
        return box.tree;
    }
}

// Shorthand interface
interface TreeConstructor<T extends HTMLable>
   extends BiFunction<List<? extends ParagraphElement>, Tags, T> {}