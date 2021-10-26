package soup.util;

import soup.util.Matcher;

public abstract class CharMatcher implements Matcher {
    protected int cnt;
    protected char last;
    protected boolean found;
    public CharMatcher() {
    }
    protected CharMatcher(CharMatcher matcher) {
        last = matcher.last;
        cnt = matcher.cnt;
        found = matcher.found;
    }
    public int matchSize() {
        return cnt;
    }
    public void reset() {
        cnt = 0;
        last = 0;
        found = false;
    }
    public boolean found() {
        return found;
    }
    abstract public CharMatcher clone();
}