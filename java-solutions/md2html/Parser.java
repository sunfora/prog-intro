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
    private IntList stack;
    // Constsants

    private static final Map<LexType, Map<Integer, TreeConstructor<ParagraphElement>>> CONSTRUCTORS = Map.of(
        LexType.DASH, Map.of(2, Strikeout::new),
        LexType.CODE, Map.of(1, Code::new, 3, Pre::new),
        LexType.STAR, Map.of(1, Emphasis::new, 2, Strong::new),
        LexType.UNDERSCORE, Map.of(1, Emphasis::new, 2, Strong::new)
    );

    // Constructors

    public Parser(Reader source) {
        lex = new Tokenizer(source);
        lastCode = new Integer[4];
        stack = new IntList();
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
        stack.clear();
        Arrays.fill(lastCode, null);
        locked = false;
        return throwBack;
    }

    private boolean update() throws IOException {
        if (!lex.hasNext() || locked) {
            return false;
        }
        tape.add(lex.next());
        //System.err.println(tape);
        switch (type(tape.back())) {
            case NL:
                return handleNL();
            case CODE:
                return handleCode();
            case STAR: case UNDERSCORE: case DASH:
                if (spaceBefore() && spaceAfter()) {
                    return true;
                }
                return handleOnStack();
        }
        return true;
    }

    private HTMLable collectBlock() throws IOException {
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
        return constructor.apply(tape.uncover(new Range(begin, end)), tags);
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

    // Handles code cases
    private boolean handleCode() {
        int lenc = len(tape.back());
        Integer lastSame = lastCode[lenc];
        lastCode[lenc] = tape.size() - 1;
        if (lastSame != null) {
            // Create box from existing tokens on tape
            Box constr = new Box(
                new Token(),
                CONSTRUCTORS.get(LexType.CODE).get(lenc).apply(
                    tape.uncover(new Range(lastSame + 1, tape.size() - 1)),
                    new Tags(tape.get(lastSame), tape.back())
                )
            );
            // Check whether there are other code tags on tape
            // If so, set null on their length in linking array
            for (int i = lastSame; i < tape.size(); ++i) {
                if (type(tape.get(i)) == LexType.CODE) {
                    lastCode[len(tape.get(i))] = null;
                }
            }
            // Apply changes
            tape.replace(
                new Range(lastSame, tape.size()),
                Collections.singletonList(constr)
            );
        }
        return true;
    }

    private void checkStacks() {

    }

    // Check if there is SPACE before last token on tape
    private boolean spaceBefore() {
        return tape.size() >= 1 && (type(tape.get(tape.size() - 1)) == LexType.SPACE);
    }

    private boolean spaceAfter() throws IOException {
        return lex.hasNext() && type(lex.showToken()) == LexType.SPACE;
    }

    // Big function to handle tokens on stack
    private boolean handleOnStack() {
        while (!stack.isEmpty() && (stack.get(-1) > (tape.size() - 1))) {
            stack.pop();
        }
        if (stack.isEmpty() || type(tape.get(stack.get(-1))) != type(tape.get(tape.size() - 1))) {
            stack.append(tape.size() - 1);
            return true;
        }
        Map<Integer, TreeConstructor<ParagraphElement>> constructors = CONSTRUCTORS.get(
            type(tape.get(stack.get(-1)))
        );
        int ln = len(tape.get(stack.get(-1)));
        if (ln == len(tape.get(tape.size() - 1)) && (constructors.get(ln) != null)) {
            tape.replace(new Range(stack.get(-1), tape.size()),
                         Collections.singletonList(new Box(
                new Token(),
                constructors.get(ln).apply(
                    tape.uncover(new Range(stack.get(-1) + 1, tape.size() - 1)),
                    new Tags(tape.get(stack.get(-1)), tape.back())
                )
            ))
            );
            stack.pop();
        } else {
            stack.append(tape.size() - 1);
        }
        return true;
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