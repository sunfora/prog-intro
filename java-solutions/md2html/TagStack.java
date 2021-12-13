package md2html;

import java.util.*;

public class TagStack {
    private final Tape tape;
    private final List<Token> tokens = new ArrayList<>();
    private final IntList ids = new IntList();
    private int length = 0;

    public TagStack(Tape tape) {
        this.tape = tape;
    }

    public int size() {
        update();
        return ids.size();
    }

    public void push(int i) {
        update();
        i = handle(i);

        if (sizeRange().contains(i)) {
            ids.append(i);
            tokens.add(tape.getToken(i));
            length += Shorts.len(tokens.get(tokens.size() - 1));
        }
    }

    public String toString() {
        update();
        return ids.toString();
    }

    public TagStack clear() {
        ids.clear();
        tokens.clear();
        return this;
    }

    public boolean isEmpty() {
        update();
        return ids.isEmpty();
    }

    public int back() {
        update();
        return ids.get(-1);
    }

    public int total() {
        update();
        return length;
    }

    public TagStack setTokensClosed() {
        update();
        for (Token tok : tokens) {
            tok.closing(true);
        }
        return this;
    }

    public List<Solution> findSolutions(SolutionFinder finder, int solutionTo) {
        update();
        if (total() < solutionTo) {
            return List.of();
        } else if (solutionTo == 0) {
            return List.of();
        }
        IntList exchange = new IntList();
        int sum = 0;
        int total = 0;
        List<Solution> result = new ArrayList<>();
        int i = -1;
        // System.err.println("searching solution for " + solutionTo);
        // System.err.println(ids);
        while ((solutionTo > 0) && (i >= -tokens.size())) {
            int j = (i + tokens.size()) % tokens.size();
            int s = Math.min(Shorts.len(tokens.get(j)), solutionTo);
            Solution solution = finder.find(s);
            if (solution.sum() > 0) {
                exchange.append(pop());
                solutionTo -= solution.sum();
                result.add(solution);
            } else {
                pop();
            }
        }
        while (!exchange.isEmpty()) {
            push(exchange.pop());
        }
        if (solutionTo > 0) {
            return List.of();
        }
        return result;
    }

    public int pop() {
        update();
        // System.err.println("tokens : " + tokens);
        // System.err.println("ids : " + ids);
        length -= Shorts.len(tokens.remove(tokens.size() - 1));
        return ids.pop();
    }

    private Range sizeRange() {
        return new Range(-tape.size(), tape.size());
    }

    private int handle(int i) {
        if (sizeRange().contains(i)) {
            return (i + tape.size()) % tape.size();
        }
        return i;
    }

    private void update() {
        if (ids.isEmpty()) {
            return;
        }
        // System.err.println("idsidsids: " + ids);
        // System.err.println("tapeididididid" + tape + " " + tape.size());
        while (!ids.isEmpty() && (!sizeRange().contains(ids.get(-1))
               || (tape.getToken(ids.get(-1)) != tokens.get(tokens.size() - 1))
               || Shorts.len(tape.get(ids.get(-1))) == 0)) {
            // System.err.println("problems " + tape.size());
            ids.pop();
            length -= Shorts.len(tokens.remove(tokens.size() - 1));
        }
    }
}