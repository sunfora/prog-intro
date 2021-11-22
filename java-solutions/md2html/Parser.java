package md2html;

import java.io.*;
import java.util.*;
import java.util.function.*;

public class Parser implements Closeable {

    // Fields

    private Lexer lex;
    private Tape tape;
    private HTMLable paragraph;
    private boolean locked;
    private boolean closed;

    private Integer[] lastCode;
    private IntList[] tagStacks;

    // Constsants

    private static final Map<LexType, List<TreeConstructor<ParagraphElement>>> CONSTRUCTORS = Map.of(
        LexType.DASH, List.of(Strikeout::new),
        LexType.CODE, List.of(Code::new, Pre::new),
        LexType.STAR, List.of(Emphasis::new, Strong::new),
        LexType.UNDERSCORE, List.of(Emphasis::new, Strong::new)
    );

    // Constructors

    public Parser(Reader source) {
        lex = new Lexer(source);
        lastCode = new Integer[4];
        tagStacks = new IntList[4];
        for (int i = 0; i < tagStacks.length; ++i) {
            tagStacks[i] = new IntList();
        }
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
            case CODE:
                return handleCode();
            case STAR: case UNDERSCORE:
                return handleEmphOrStr();
            case DASH:
                return handleStrike();
            case SPACE:
                return handleSpace();
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

    // Handles space tokens
    // (if backing token on tape is ** or __ or -- it removes them from stack)
    private boolean handleSpace() {
        if (tape.size() > 1) {
            int id = tape.size() - 2;
            switch (type(tape.get(id))) {
                case STAR: case UNDERSCORE: case DASH:
                    IntList stack = tagStacks[type(tape.get(id)).ordinal()];
                    if (!stack.isEmpty()) {
                        stack.pop();
                    }
            }
        }
        return true;
    }

    // Handles Strong and Emphasis cases
    private boolean handleEmphOrStr() {
        return handleOnStack(new IntList(1, 2), CONSTRUCTORS.get(type(tape.back())));
    }

    // Handles Strikout cases
    private boolean handleStrike() {
        return handleOnStack(new IntList(2), CONSTRUCTORS.get(type(tape.back())));
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
                CONSTRUCTORS.get(LexType.CODE).get((lenc < 3)? 0: 1).apply(
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

    private class Tape extends ArrayList<Box> {
        public Tape() {
            super();
        }

        public Box back() {
            return tape.get(tape.size() - 1);
        }

        public Box pop() {
            return tape.remove(tape.size() - 1);
        }

        public void add(Token token) {
            add(new Box(token, null));
        }

        public Token getToken(int id) {
            return get(id).token;
        }

        public ParagraphElement getTree(int id) {
            return tree(get(id));
        }

        public List<ParagraphElement> uncover(Range range) {
            ArrayList<ParagraphElement> exchange = new ArrayList<>();
            for (int i = range.inf; i < range.sup; ++i) {
                exchange.add(getTree(i));
            }
            return exchange;
        }

        public void replace(Range range, Collection<? extends Box> rep) {
            rep = new ArrayList<>(rep);
            List<Box> sub = subList(range.inf, range.sup);
            sub.clear();
            sub.addAll(rep);
        }
    }

    // Shorthands and implementations

    // Returns id of biggest suitable length in lengths list
    private Integer getLenId(int size, IntList lengths) {
        for (int i = -1; i >= -lengths.size(); --i) {
            if (lengths.get(i) <= size) {
                return lengths.size() + i;
            }
        }
        return null;
    }

    // Check if there is SPACE before last token on tape
    private boolean spaceBefore() {
        return (type(tape.get(tape.size() - 2)) == LexType.SPACE);
    }

    // Big function to handle tokens on stack
    private boolean handleOnStack(IntList lengths, List<TreeConstructor<ParagraphElement>> constructors) {
        Token got = tape.back().token;
        IntList stack = tagStacks[type(got).ordinal()];
        Range sizes = new Range(lengths.get(0), lengths.get(-1) + 1);
        // Check if token can be interpreted as anything valid
        if (!sizes.contains(len(got))) {
            return true;
        }
        // Append token if it is in no possible way is closing
        if (stack.isEmpty() || spaceBefore()) {
            stack.append(tape.size() - 1);
            return true;
        }
        // Get last valid token before
        int idOfLast = stack.get(-1);
        Token last = tape.getToken(idOfLast);
        int size = Math.min(len(got), len(last));
        int del = 0;
        // Iteratively apply fitting constructors
        while (sizes.contains(size - del)) {
            int id = getLenId(size, lengths);
            int t = lengths.get(id);
            del += t;
            String raw = got.substring(0, t);
            tape.replace(
                new Range(idOfLast + 1, tape.size() - 1),
                Collections.singletonList(
                    new Box(
                        new Token(),
                        constructors.get(id).apply(
                            tape.uncover(new Range(idOfLast + 1, tape.size() - 1)),
                            new Tags(raw, raw)
                        )
                    )
                )
            );
        }
        // Reset opening and closing tokens
        got.setLength(len(got) - del);
        last.setLength(len(last) - del);
        // Check which token remains in stack
        if (len(last) == 0) {
            stack.pop();
        }
        if (sizes.contains(len(got))) {
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

// Pair with token and tree
class Box {
    public final Token token;
    public final ParagraphElement tree;

    public Box(Token token, ParagraphElement tree) {
        this.token = token;
        this.tree = tree;
    }

    public Box(Box box) {
        this(box.token, box.tree);
    }

    public String toString() {
        return token.toString();
    }
}