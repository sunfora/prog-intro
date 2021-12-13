package expression.parser;

import expression.*;
import java.math.BigInteger;
import java.util.function.*;

public class ExpressionParser implements Parser {

    public PolyExpression parse(final String data) {
/*         System.err.println("test = `" + data + "`");
        WhiteHate WH = new WhiteHate(new StringSource(data));
         System.err.print("WhiteHate version : `");
        while (WH.hasNext()) {
             System.err.print(WH.next());
        }
         System.err.println("`");                          */
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
            throw error("End of expression expected");
        }

        PolyExpression parseExpression() {
            return parseP(3);
        }

        Variable parseVariable() {
            for (char var : "xyz".toCharArray()) {
                if (take(var)) {
                    return new Variable(var);
                }
            }
            throw error("Not supported variable");
        }

        Operation parseUnary(Op op) {
            if (op.isUnary()) {
                return op.make(parseP0());
            }
            throw error("Not an unary operation" + op);
        }

        Op parseSign() {
            Op result;
            take(' ');
            if (take('*')) {
                result = Op.MULTIPLY;
            } else if (take('/')) {
                result = Op.DIVIDE;
            } else if (take('+')) {
                result = Op.ADD;
            } else if (take('-')) {
                result = Op.SUBTRACT;
            } else if (take('m')) {
                if (take('i')) {
                    expect('n');
                    result = Op.MIN;
                } else {
                    expect("ax");
                    result = Op.MAX;
                }
            } else if (take('t')) {
                expect('0');
                result = Op.T0;
            } else if (take('l')) {
                expect('0');
                result = Op.L0;
            } else {
                result = Op.NONE;
            }
            take(' ');
            return result;
        }

        Const parseConst(boolean minus) {
            StringBuilder sb = new StringBuilder();
            if (minus) {
                sb.append('-');
            }
            if (take('0')) {
                return new Const(0);
            }
            do {
                sb.append(take());
            } while (between('0', '9'));
            return new Const(new BigInteger(sb.toString()));
        }

        PolyExpression parseP(int priority) {
            if (priority == 0) {
                return parseP0();
            }
            PolyExpression result = parseP(priority - 1);
            while (sign.getPriority() == priority) {
                result = sign.make(result, parseP(priority - 1));
            }
            return result;
        }

        PolyExpression parseP0() {
            PolyExpression result;
            Op unary = Op.NONE;
            take(' ');
            if (take('(')) {
                result = parseExpression();
                expect(')');
            } else {
                boolean minus = false;
                if (take('-')) {
                    minus = between('0', '9');
                    unary = minus? unary : Op.NEG;
                } else {
                    unary = parseSign();
                }
                if (unary != Op.NONE) {
                    result = parseUnary(unary);
                } else {
                    if (between('x', 'z')) {
                        result = parseVariable();
                    } else if (between('0', '9')) {
                        result = parseConst(minus);
                    } else {
                        throw error("Not P0");
                    }
                }
            }
            take(' ');
            sign = (unary == Op.NONE)? parseSign() :  sign;
            return result;
        }
    }

    public static enum Op {
        MULTIPLY (Multiply::new),
        DIVIDE   (Divide::new),
        ADD      (Add::new),
        SUBTRACT (Subtract::new),
        MIN      (Min::new),
        MAX      (Max::new),
        NEG      (Neg::new),
        T0       (T0::new),
        L0       (L0::new),
        NONE     ();

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
                return "None";
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