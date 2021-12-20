package expression.parser;

import expression.*;
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

    public PolyExpression parse(final String data) {
        return parse(new WhiteHate(new StringSource(data)));
    }

    private PolyExpression parse(final CharSource source) {
        return new ExpressionMaker(source).parse();
    }

    private static class ExpressionMaker extends BaseParser {
        PolyExpression expr;
        Op sign = Op.NONE;

        public ExpressionMaker(final CharSource source) {
            super(source);
        }

        public PolyExpression parse() {
            PolyExpression result = parseExpression();
            if (eof()) {
                return result;
            }
            throw error("end of expression expected");
        }

        PolyExpression parseExpression() {
            PolyExpression result = parseP(MIN_VALUE);
            take(' ');
            return result;
        }

        int lower(Op op) {
            return sign.getPriority() - 1;
        }

        void updateSign() {
            if (sign == Op.NONE) {
                sign = parseBinarySign();
            }
        }

        PolyExpression parseP(int priority) {
            PolyExpression result;
            result = parseP0();
            updateSign();
            int fPos = pos();
            Op fOp = Op.NONE;
            while ((sign != Op.NONE) && (lower(sign) < priority)) {
                fPos = pos();
                fOp = sign;
                result = sign.make(
                    result,
                    parseP(Util.fall(lower(sign), sign = Op.NONE))
                );
            }
            return result;
        }

        PolyExpression parseP0() {
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
                        throw error("empty expression");
                    }
                }
            }
            take(' ');
            return result;
        }

        Variable parseVariable() {
            for (char var : "xyz".toCharArray()) {
                if (take(var)) {
                    return new Variable(var);
                }
            }
            throw error("variable not found");
        }

        Operation parseUnary(Op op) {
            int fPos = pos();
            return op.make(parseP0());
        }

        Const parseConst(boolean minus) {
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
            result = new Const(Integer.parseInt(sb.toString()));
            return result;
        }

        Op parseBinarySign() {
            Op result = take('+') ? Op.ADD
                      : take('-') ? Op.SUBTRACT
                      : take('*') ? Op.MULTIPLY
                      : take('/') ? Op.DIVIDE
                      : take('m') ? take('i')? expect('n', Op.MIN) : expect("ax", Op.MAX)
                      : Op.NONE;
            return result;
        }

        Op parseUnarySign() {
            Op result = take('-')? Op.NEG
                      : take('t')? expect('0', Op.T0)
                      : take('l')? expect('0', Op.L0)
                      : Op.NONE;
            return result;
        }
    }

    public static enum Op {
        NONE     (             ),
        NEG      (   Neg::new  ),
        T0       (   T0::new   ),
        L0       (   L0::new   ),
        MULTIPLY (Multiply::new),
        DIVIDE   ( Divide::new ),
        ADD      (   Add::new  ),
        SUBTRACT (Subtract::new),
        MIN      (   Min::new  ),
        MAX      (   Max::new  );

        private final BiFunction<PolyExpression, PolyExpression, ? extends Operation> binary;
        private final Function<PolyExpression, ? extends Operation> unary;
        private final Operation example;
        
        private Op() {
            binary = null;
            unary = null;
            this.example = null;
        }

        private Op(BiFunction<PolyExpression, PolyExpression, ? extends Operation> binary) {
            this.binary = binary;
            this.unary = null;
            this.example = binary.apply(new Variable('x'), new Variable('y'));
        }

        private Op(Function<PolyExpression, ? extends Operation> unary) {
            this.unary = unary;
            this.binary = null;
            this.example = unary.apply(new Variable('x'));
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
    }
}