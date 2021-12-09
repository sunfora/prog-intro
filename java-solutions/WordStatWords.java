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
import java.util.Objects;

public class WordStatWords {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("2 files have to be specified");
            return;
        }
        String inputPath = args[0];
        String outputPath = args[1];
        String[] words;
        IntPair[] stats;
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
                stats = getStats(words);
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
                for (int i = 0; i < stats.length; i++) {
                    int ind = stats[i].first;
                    int cnt = stats[i].second;
                    String word = words[ind];
                    output.write(word + " " + cnt);
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

    public static IntPair[] getStats(String[] words) {
        Arrays.sort(words);
        IntPair[] stats = new IntPair[1];
        int j = 0;
        int i = 0;
        while (j < words.length) {
            int ub = upperBound(words, j, words.length, words[j]);
            int cnt = ub - j;
            if (i >= stats.length) {
                stats = Arrays.copyOf(stats, stats.length * 2);
            }
            stats[i++] = new IntPair(j, cnt);
            j = ub;
        }
        int size = i;
        return Arrays.copyOf(stats, size);
    }

    public static int upperBound(String[] words, int lb, int rb, String key) {
        while (rb - lb > 1) {
            int mid = lb + (rb - lb)/2;
            String mword = words[mid];
            int cmp = key.compareTo(mword);
            if (cmp < 0) {
                rb = mid;
            } else {
                lb = mid;
            }
        }
        return rb;
    }

    public static String[] readWords(WordReader input) throws IOException {
        String[] words = new String[1];
        int cnt = 0;
        while (input.hasNextWord()) {
            if (cnt >= words.length) {
                words = Arrays.copyOf(words, words.length * 2);
            }
            words[cnt++] = input.nextWord().toLowerCase();
        }
        return Arrays.copyOf(words, cnt);
    }
}

class IntPair {

    public int first;
    public int second;

    public IntPair(int first, int second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}
