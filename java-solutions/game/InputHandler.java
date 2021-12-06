package game;

import java.io.*;
import java.util.*;
import java.util.function.*;

class InputHandler implements Closeable {
// #fields:
    private PrintStream out;
    private InputStream in;
    private Scanner scan;
    private boolean closed;
    private NoSuchElementException lineError;
// fields#

// #constructors:
    public InputHandler(InputStream in, PrintStream out) {
        this.out = out;
        this.in = in;
        this.scan = new Scanner(in);
    }
// constructors#

// #public:
    public <S> Result<S> askWithAgreement(
        ReadingFunction<S> reader, String msg, String err
    ) throws IOException {
        Result<S> result;
        Result<Boolean> accept;
        do {
            result = ask(reader, msg, err);
            accept = ask(this::readYesNo, "Are you sure? Y/N");
        } while (!accept.isValid() || !accept.getValue());
        return result;
    }

    public <S> Result<S> askWithAgreement(ReadingFunction<S> f, String m) throws IOException {
        return askWithAgreement(f, m, null);
    }

    public <S> Result<S> askWithAgreement(ReadingFunction<S> f) throws IOException {
        return askWithAgreement(f, null);
    }

    public <S> Result<S> ask(ReadingFunction<S> reader, String msg, String err) throws IOException {
        Result<S> result = new Result<>(false, null);
        do {
            if (null != msg) {
                out.println(msg);
            }
            result = reader.apply();
            if (null != err && !result.isValid()) {
                out.println(err);
            }
        } while(checkLine(!result.isValid()));
        return result;
    }

    public <S> Result<S> ask(ReadingFunction<S> reader, String msg) throws IOException {
        return ask(reader, msg, null);
    }

    public <S> Result<S> ask(ReadingFunction<S> reader) throws IOException {
        return ask(reader, null);
    }

    public Result<List<Integer>> readIntegerList() throws IOException {
        final List<Integer> result = new ArrayList<>();
        Result<String> line = readLine();
        Result<List<Integer>> rValue = new Result<>(false, result);
        if (line.isValid()) {
            Scanner ints = new Scanner(line.getValue());
            while (ints.hasNextInt()) {
                result.add(ints.nextInt());
            }
            if (!ints.hasNext()) {
                rValue = new Result<>(true, result);
            }
            ints.close();
        }
        return rValue;
    }

    public Result<List<Integer>> readIntegerList(int cnt) throws IOException {
        Result<List<Integer>> result = readIntegerList();
        if ((result.isValid()) && (result.getValue().size() == cnt)) {
            return result;
        }
        return new Result<>(false, result.getValue());
    }

    public Result<Integer> readInt() throws IOException {
        Result<List<Integer>> result = readIntegerList(1);
        if (result.isValid()) {
            return new Result<>(true, result.getValue().get(0));
        }
        return new Result<>(false, (result.getValue().size() > 0)? result.getValue().get(0) : null);
    }

    public Result<IntPair> readIntPair() throws IOException {
        Result<List<Integer>> result = readIntegerList(2);
        if (result.isValid()) {
            return new Result<>(true, new IntPair(result.getValue()));
        }
        return new Result<>(
            false,
            (result.getValue().size() > 2)
            ? new IntPair(result.getValue().subList(0, 2))
            : null
        );
    }

    public Result<List<String>> readStrings() throws IOException {
        Result<String> line = readLine();
        List<String> strings = new ArrayList<>();
        if (line.isValid()) {
            Scanner scan = new Scanner(line.getValue());
            while (scan.hasNext()) {
                strings.add(scan.next());
            }
            scan.close();
            if (strings.size() != 0) {
                return new Result<>(true, strings);
            }
        }
        return new Result<>(false, strings);
    }

    public Result<String> readLine() throws IOException {
        if (scan.hasNextLine()) {
            return checkIO(new Result<>(true, scan.nextLine()));
        }
        lineError = new NoSuchElementException("Cannot read line from input " + in);
        return checkIO(new Result<>(false, null));
    }

    public Result<Boolean> readYesNo() throws IOException {
        Result<String> result = readLine();
        if (result.isValid()) {
            switch (result.getValue().strip().toLowerCase()) {
                case "yes": case "y":
                    return new Result<>(true, true);
                case "no": case "n": case "not":
                    return new Result<>(true, false);
            }
        }
        return new Result<>(false, null);
    }
// public#

// #implemented:
    @Override
    public void close() {
        if (!closed) {
            try {
                scan.close();
            } finally {
                closed = true;
                in = null;
                out = null;
                scan = null;
            }
        }
    }
// implemented#

// #private:
    private <S> S checkIO(S throwBack) throws IOException {
        if (null != scan.ioException()) {
            throw scan.ioException();
        }
        if (out.checkError()) {
            throw new IOException("PrintStream detected IOException");
        }
        return throwBack;
    }

    private <S> S checkLine(S throwBack) throws IOException {
        if (null != lineError) {
            throw lineError;
        }
        return throwBack;
    }
// private#
}

/*
class NonEmpty implements Function<String, Result<String>> {
    public Result<String> apply(String str) {
        return ("".equals(str))
            ? new Result<>(false, null)
            : new Result<>(true, str);
    }
    }
    */