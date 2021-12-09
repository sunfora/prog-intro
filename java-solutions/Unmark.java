// Class to perform unmarking of numbers for example "0xFFF" should be "FFF"
// before passing it to the decoder
public class Unmark implements NumberDecoder {

    NumberDecoder wrapped;

    public Unmark(NumberDecoder wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public Number decode(String token, int radix) {
        switch (radix) {
            case 16:
            case 8:
            case 2:
                token = deleteMarker(token, radix);
        }
        return wrapped.decode(token, radix);
    }

    // Returns a new string without marker
    // marker 0x, 0X, # : hex
    // marker 0b, 0B    : binary
    // marker 0o, 0O, 0 : octal
    public static String deleteMarker(String token, int radix) {
        if (token.length() == 0) {
            throw new NumberFormatException("Zero length number");
        }
        String[] markers;
        switch (radix) {
            case 16:
                markers = new String[]{"#", "0x", "0X"};
                break;
            case 8:
                markers = new String[]{"0o", "0O", "0"};
                break;
            case 2:
                markers = new String[]{"0b", "0B"};
                break;
            default:
                throw new IllegalArgumentException(radix + ": is not supported");
        }
        char first = token.charAt(0);
        int signLen = ((first == '+') || (first == '-'))? 1: 0;
        int markerLen = 0;
        for (String marker : markers) {
            if (token.startsWith(marker, signLen)) {
                markerLen = marker.length();
            }
        }
        int markerEnd = signLen + markerLen;
        return token.substring(0, signLen) + token.substring(markerEnd);
    }

    @Override
    public String toString() {
        return super.toString() + " wrapping " + wrapped.toString();
    }
}