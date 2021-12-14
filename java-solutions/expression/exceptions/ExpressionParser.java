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
        IllegalOperationTypeException,
        ConstOverflowException,
        OperandNotFoundException,
        EndExpectedException
    {
//        System.err.println('`' + data + '`');
        return parse(new WhiteHate(new StringSource(data)));
    }

    private PolyExpression parse(final CharSource source) throws
        ExpectedSymbolException,
        IllegalOperationTypeException,
        ConstOverflowException,
        EndExpectedException,
        OperandNotFoundException
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
            IllegalOperationTypeException
        {
            PolyExpression result = parseExpression();
            if (eof()) {
                return result;
            }
            throw new EndExpectedException(pos(), " end of expression expected");
        }

        PolyExpression parseExpression() throws
            ExpectedSymbolException,
            OperandNotFoundException,
            ConstOverflowException,
            IllegalOperationTypeException
        {
            PolyExpression result =  parseP(MIN_VALUE);
            if (sign != Op.NONE) {
                throw rightOperandMissing(pos(), sign);
            }
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
            IllegalOperationTypeException,
            OperandNotFoundException,
            ExpectedSymbolException,
            ConstOverflowException
        {
            if (op.isUnary()) {
                try {
                    return op.make(parseP0());
                } catch (OperandNotFoundException e) {
                    throw rightOperandMissing(pos(), op);
                }
            } else if (op.isBinary()) {
                throw leftOperandMissing(pos(), op);
            }
            throw new IllegalOperationTypeException(
                pos(), op + " is not an unary operation"
            );
        }

        Op parseSign() throws ExpectedSymbolException {
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

        PolyExpression parseP(int priority) throws
            ExpectedSymbolException,
            OperandNotFoundException,
            IllegalOperationTypeException,
            ConstOverflowException
        {
            if (priority == 0) {
                return parseP0();
            }
            PolyExpression result = parseP(priority - 1);
            try {
                while (sign.getPriority() == priority) {
                    result = sign.make(result, parseP(priority - 1));
                }
            } catch (OperandNotFoundException e) {
                throw rightOperandMissing(pos(), sign);
            }
            return result;
        }

        PolyExpression parseP0() throws
            ExpectedSymbolException,
            OperandNotFoundException,
            IllegalOperationTypeException,
            ConstOverflowException
        {
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
                        throw new OperandNotFoundException(pos(), "primary expression not found");
                    }
                }
            }
            take(' ');
            if (unary == Op.NONE) {
                sign = parseSign();
                if (!sign.isBinary() && sign != Op.NONE) {
                    throw new IllegalOperationTypeException(
                        pos(), sign + " is not a binary operation"
                    );
                }
            }
            return result;
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
        MULTIPLY (CheckedMultiply::new),
        DIVIDE   (CheckedDivide::new),
        ADD      (CheckedAdd::new),
        SUBTRACT (CheckedSubtract::new),
        NEG      (CheckedNegate::new),
        T0       (T0::new),
        L0       (L0::new),
        MIN      (Min::new),
        MAX      (Max::new),
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