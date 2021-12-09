import java.io.Closeable;
import java.util.Comparator;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.util.Arrays;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;

public class WordStatInput {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("2 files have to be specified");
            return;
        }
        String inputPath = args[0];
        String outputPath = args[1];
        System.err.println("files are: " + inputPath + " " + outputPath);
        StringWithInt[] words;
        try {
            WordReader input = new WordReader(
                new BufferedReader(
                    new InputStreamReader(
                        new FileInputStream(inputPath),
                        "utf8"
                    )
                )
            );
            try {
                words = readWords(input);
                words = getStats(words);
            } finally {
                input.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Input file not found: " + e.getMessage());
            return;
        } catch (IOException e) {
            System.out.println("IOException occured: " + e.getMessage());
            return;
        }
        try {
            BufferedWriter output = new BufferedWriter(
                new OutputStreamWriter(
                    new FileOutputStream(outputPath),
                    "utf8"
                )
            );
            try {
                for (int i = 0; i < words.length; i++) {
                    String str = words[i].string.toLowerCase();
                    int cnt = words[i].integer;
                    output.write(str + " " + cnt);
                    output.newLine();
                }
            } finally {
                output.close();
            }
        } catch(FileNotFoundException e) {
            System.out.println("Cannot reach output file while writing: " + e.getMessage());
        } catch(IOException e) {
            System.out.println("IOexception occured: " + e.getMessage());
        }
    }

    /*data in words will be modified*/
    public static StringWithInt[] getStats(StringWithInt[] words) {
        Arrays.sort(words);
        IntPair[] stats = new IntPair[1];
        int j = 0;
        int i = 0;
        while (j < words.length) {
            int ind = words[j].integer;
            int ub = upperBound(words, j, words[j]);
            int cnt = ub - j;
            if (i >= stats.length) {
                stats = Arrays.copyOf(stats, stats.length * 2);
            }
            stats[i] = new IntPair(ind, cnt);
            words[i] = words[j];
            j = ub;
            i += 1;
        }
        int size = i;
        /*rearange it in a normal order*/
        Arrays.sort(words, 0, size, new ComparatorByInt());
        Arrays.sort(stats, 0, size);
        /*now we need to replace indexes by values of stats*/
        for (int k = 0; k < size; k++) {
            int cnt = stats[k].second;
            words[k].integer = cnt;
        }
        return Arrays.copyOf(words, size);
    }

    public static int upperBound(StringWithInt[] words, int lb, StringWithInt key) {
        int rb = words.length;
        while (rb - lb > 1) {
            int mid = lb + (rb - lb)/2;
            StringWithInt mword = words[mid];
            int cmp = key.compareTo(mword);
            if (cmp < 0) {
                rb = mid;
            } else {
                lb = mid;
            }
        }
        return rb;
    }

    public static StringWithInt[] readWords(WordReader input) throws IOException {
        StringWithInt[] words = new StringWithInt[1];
        int wordsCnt = 0;
        while (input.hasNextWord()) {
            wordsCnt += 1;
            if (wordsCnt > words.length) {
                words = Arrays.copyOf(words, words.length * 2);
            }
            int ind = wordsCnt - 1;
            words[ind] = new StringWithInt(input.getNextWord(), ind);
        }
        return Arrays.copyOf(words, wordsCnt);
    }
}

class StringWithInt implements Comparable<StringWithInt> {
    public String string;
    public int integer;

    public StringWithInt(String string, int integer) {
        this.string  = string;
        this.integer = integer;
    }

    @Override
    public String toString() {
        return "(" + string + ", " + integer + ")";
    }

    @Override
    public int compareTo(StringWithInt other) {
        int lenThis = this.string.length();
        int lenOther = other.string.length();
        if (lenThis < lenOther) {
            return -1;
        } else if (lenThis > lenOther) {
            return 1;
        }
        return this.string.compareToIgnoreCase(other.string);
    }

    public int compareToByInt(StringWithInt other) {
        return Integer.compare(this.integer, other.integer);
    }
}

class ComparatorByInt implements Comparator<StringWithInt> {
    @Override
    public int compare(StringWithInt first, StringWithInt second) {
        return first.compareToByInt(second);
    }
}