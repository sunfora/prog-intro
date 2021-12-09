public interface NumberDecoder {
    public Number decode(String str, int radix);
}

class IntDecoder implements NumberDecoder {
    public Integer decode(String str, int radix) {
        return Integer.parseInt(str, radix);
    }
}

class UnsignedIntDecoder implements NumberDecoder {
    public Integer decode(String str, int radix) {
        return Integer.parseUnsignedInt(str, radix);
    }
}

class AutoDec implements NumberDecoder {
    public final int radix;
    public final NumberDecoder decoder;
    public AutoDec(NumberDecoder decoder, int radix) {
        this.radix = radix;
        this.decoder = decoder;
    }
    public Number decode(String number) {
        return decoder.decode(number, radix);
    }
    @Override
    public Number decode(String number, int radix) {
        return decoder.decode(number, radix);
    }
}