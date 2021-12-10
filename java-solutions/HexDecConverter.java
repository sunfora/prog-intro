import java.io.*;
import java.util.*;

public class HexDecConverter {

    private Split.View source;
    private int number;
    private AutoDec hexDec = new AutoDec(new Unmark(new UnsignedIntDecoder()), 16);
    private AutoDec decDec = new AutoDec(new IntDecoder(), 10);
    private boolean found;

    public HexDecConverter(Split.View source) {
        this.source = source;
    }

    private boolean probablyHex(String number) {
        return ((number.length() > 2) && (Character.toLowerCase(number.charAt(1)) == 'x'));
    }

    public boolean hasNext() throws IOException {
        for (; !found && source.hasNext(); source.next()) {
                String token = source.showToken();
		if (found = !"".equals(token)) {
                    AutoDec dec = (probablyHex(token))? hexDec : decDec;
                    number = dec.decode(token).intValue();
		    found = true;
		}
        }
        return found;
    }

    public int next() throws IOException {
        if (!hasNext()) {
            throw new NoSuchElementException("No more hex/dec nums");
        }
        found = false;
        return number;
    }
}
