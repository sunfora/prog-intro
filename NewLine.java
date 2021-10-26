package soup.util;

import soup.util.CharMatcher;

public class NewLine extends CharMatcher {
    public NewLine() {
        super();
    }
    public NewLine(NewLine other) {
        super(other);
    }
    @Override
    public void send(char character) {
        if (found) {
            found = false;
            cnt = 0;
        }
        switch (character) {
           case '\r':
           case '\n':
           case '\f':
           case '\u000b':
           case '\u2028':
           case '\u2029':
           case '\u0085':
               found = true;
               cnt = 1;
        }
        if ('\r' == last && '\n' == character) {
            cnt++;
        }
        last = character;
    }
    @Override
    public NewLine clone() {
        return new NewLine(this);
    }
}
