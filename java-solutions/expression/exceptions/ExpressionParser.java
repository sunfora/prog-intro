package expression.exceptions;

import expression.*;
import expression.parser.StringSource;
import expression.parser.WhiteHate;
import expression.parser.CharSource;
import java.util.function.*;

public class ExpressionParser implements Parser {

    private final static int MIN_VALUE;

    static {
        int lowest = 0;
        for (Op op : Op.values()) {
            lowest = Math.max(lowest, op.getPriority());
        }
        MIN_VALUE = lowest;
    }

    public PolyExpression parse(final String data) throws
        ExpectedSymbolException,
        ConstOverflowException,
        OperandNotFoundException,
        EndExpectedException,
        NotAnOperationException
    {
        //System.err.println("`" + data + "`");
        return parse(new WhiteHate(new StringSource(data)));
    }

    private PolyExpression parse(final CharSource source) throws
        ExpectedSymbolException,
        ConstOverflowException,
        EndExpectedException,
        OperandNotFoundException,
        NotAnOperationException
    {
        return new ExpressionMaker(source).parse();
    }

    private static class ExpressionMaker extends BaseParser {
        PolyExpression expr;
        Op sign = Op.NONE;

        public ExpressionMaker(final CharSource source) {
            super(source);
        }

        public PolyExpression parse() throws
            EndExpectedException,
            ExpectedSymbolException,
            ConstOverflowException,
            OperandNotFoundException,
            NotAnOperationException
        {
            PolyExpression result = parseExpression();
            if (eof()) {
                return result;
            }
            if (take(')')) {
                throw new ExpectedSymbolException(
                    pos(), ' ', ')', "missing opening bracket"
                );
            }
            throw new EndExpectedException(pos(), " end of expression expected");
        }

        PolyExpression parseExpression() throws
            ExpectedSymbolException,
            OperandNotFoundException,
            ConstOverflowException,
            NotAnOperationException
        {
            PolyExpression result = parseP(MIN_VALUE);
            if (sign != Op.NONE) {
                throw rightOperandMissing(pos(), sign);
            }
            take(' ');
            return result;
        }

        int lower(Op op) {
            return sign.getPriority() - 1;
        }

        void updateSign() throws ExpectedSymbolException, NotAnOperationException {
            if (sign == Op.NONE) {
                sign = parseBinarySign();
            }
        }

        PolyExpression parseP(int priority) throws
            ExpectedSymbolException,
            OperandNotFoundException,
            ConstOverflowException,
            NotAnOperationException
        {
            PolyExpression result;
            result = parseP0();
            updateSign();
            int fPos = pos();
            Op fOp = Op.NONE;
            try {
                while ((sign != Op.NONE) && (lower(sign) < priority)) {
                    fPos = pos();
                    fOp = sign;
                    result = sign.make(
                        result,
                        parseP(Util.fall(lower(sign), sign = Op.NONE))
                    );
                }
            } catch (OperandNotFoundException e) {
                throw rightOperandMissing(fPos, fOp);
            }
            return result;
        }

        PolyExpression parseP0() throws
            ExpectedSymbolException,
            OperandNotFoundException,
            ConstOverflowException,
            NotAnOperationException
        {
            int fPos = pos();
            PolyExpression result;
            Op unary = Op.NONE;
            take(' ');
            if (take('(')) {
                result = parseExpression();
                expect(')');
            } else {
                unary = parseUnarySign();
                if (unary != Op.NONE && !(unary == Op.NEG && between('0', '9'))) {
                    result = parseUnary(unary);
                } else {
                    if (between('x', 'z')) {
                        result = parseVariable();
                    } else if (between('0', '9')) {
                        result = parseConst(unary == Op.NEG);
                    } else {
                        if ((sign = parseBinarySign()) != Op.NONE) {
                            throw leftOperandMissing(fPos, sign);
                        } else {
                            throw new OperandNotFoundException(
                                fPos, "empty expression"
                            );
                        }
                    }
                }
            }
            take(' ');
            return result;
        }

        Variable parseVariable() throws OperandNotFoundException {
            for (char var : "xyz".toCharArray()) {
                if (take(var)) {
                    return new Variable(var);
                }
            }
            throw new OperandNotFoundException(pos(), "variable not found");
        }

        Operation parseUnary(Op op) throws
            OperandNotFoundException,
            ExpectedSymbolException,
            ConstOverflowException,
            NotAnOperationException
        {
            int fPos = pos();
            try {
                return op.make(parseP0());
            } catch (OperandNotFoundException e) {
                throw rightOperandMissing(fPos, op);
            }
        }

        Const parseConst(boolean minus) throws ConstOverflowException {
            StringBuilder sb = new StringBuilder();
            Const result;
            if (minus) {
                sb.append('-');
            }
            if (take('0')) {
                return new Const(0);
            }
            do {
                sb.append(take());
            } while (between('0', '9'));
            try {
                result = new Const(Integer.parseInt(sb.toString()));
            } catch (NumberFormatException e) {
                throw new ConstOverflowException(
                    pos(), "expected int, found : " + sb, e
                );
            }
            return result;
        }

        Op parseBinarySign() throws ExpectedSymbolException, NotAnOperationException {
            StringBuilder word = new StringBuilder()
                .append(Util.isWord(back)? back : "");

            Op result = take('<') ? expect('<', Op.L_SHIFT)
                      : take('+') ? Op.ADD
                      : take('-') ? Op.SUBTRACT
                      : take('*') ? take('*')? Op.POW : Op.MULTIPLY
                      : take('/') ? take('/')? Op.LOG : Op.DIVIDE
                      : take('>') ? expect('>') && take('>')? Op.TR_SHIFT : Op.R_SHIFT
                      : take('m') ? take('i')? expect('n', Op.MIN) : expect("ax", Op.MAX)
                      : Op.NONE;

            if (result.isWord()) {
                ensureSignWord(word, result);
            }
            return result;
        }

        Op parseUnarySign() throws ExpectedSymbolException, NotAnOperationException {
            StringBuilder word = new StringBuilder()
                .append(Util.isWord(back)? back : "");

            Op result = take('-')? Op.NEG
                      : take('t')? expect('0', Op.T0)
                      : take('l')? expect('0', Op.L0)
                      : take('a')? expect("bs", Op.ABS)
                      : Op.NONE;

            if (result.isWord()) {
                ensureSignWord(word, result);
            }
            return result;
        }

        void ensureSignWord(StringBuilder word, Op result) throws NotAnOperationException {
            boolean fail = word.length() > 0 && result.isWord();
            word.append(result.getOperation());
            int sz = word.length();
            for (; Util.isWord(ch); word.append(take()));
            if (fail || sz < word.length()) {
                throw new NotAnOperationException(
                    pos(), word.toString()
                );
            }
        }
    }

    private static OperandNotFoundException rightOperandMissing(int pos, Op op) {
        return new OperandNotFoundException(
            pos, op + " right operand missing"
        );
    }

    private static OperandNotFoundException leftOperandMissing(int pos, Op op) {
        return new OperandNotFoundException(
            pos, op + " left operand missing"
        );
    }

    public static enum Op {
        NONE     (                    ),
        NEG      ( CheckedNegate::new ),
        T0       (      T0::new       ),
        L0       (      L0::new       ),
        ABS      (      Abs::new      ),
        POW      (      Pow::new      ),
        LOG      (      Log::new      ),
        MULTIPLY (CheckedMultiply::new),
        DIVIDE   ( CheckedDivide::new ),
        ADD      (   CheckedAdd::new  ),
        SUBTRACT (CheckedSubtract::new),
        R_SHIFT  (     RShift::new    ),
        L_SHIFT  (     LShift::new    ),
        TR_SHIFT (    TrShift::new    ),
        MIN      (      Min::new      ),
        MAX      (      Max::new      );

        private final BiFunction<PolyExpression, PolyExpression, ? extends Operation> binary;
        private final Function<PolyExpression, ? extends Operation> unary;
        private final Operation example;
        private final boolean word;

        private Op() {
            binary = null;
            unary = null;
            this.example = null;
            word = false;
        }

        private Op(BiFunction<PolyExpression, PolyExpression, ? extends Operation> binary) {
            this.binary = binary;
            this.unary = null;
            this.example = binary.apply(new Variable('x'), new Variable('y'));
            word = Util.isWord(getOperation());
        }

        private Op(Function<PolyExpression, ? extends Operation> unary) {
            this.unary = unary;
            this.binary = null;
            this.example = unary.apply(new Variable('x'));
            word = Util.isWord(getOperation());
        }

        @Override
        public String toString() {
            if (null == example) {
                return "";
            }
            return example.toString();
        }

        public Operation make(PolyExpression min1, PolyExpression min2) {
            if (!isBinary()) {
                throw new IllegalStateException("Not a binary operation");
            }
            return binary.apply(min1, min2);
        }

        public int getPriority() {
            if (null == example) {
                return -1;
            }
            return example.getPriority();
        }

        public String getOperation() {
            if (null == example) {
                return "None";
            }
            return example.getOperation();
        }

        public Operation make(PolyExpression min1) {
            if (!isUnary()) {
                throw new IllegalStateException("Not an unary operation");
            }
            return unary.apply(min1);
        }

        public boolean isUnary() {
            return unary != null;
        }

        public boolean isBinary() {
            return binary != null;
        }

        public boolean isWord() {
            return word;
        }
    }
}