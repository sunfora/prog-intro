import java.io.*;
import java.util.*;

public class Wspp {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Expected 2 files in args");
            return;
        }
        String ipath = args[0];
        String opath = args[1];
        try {
            Reader input = new InputStreamReader(new FileInputStream(ipath), "utf8");
            Writer output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(opath), "utf8"));
            TerribleSplit split = new TerribleSplit(input, new NewLine(), new NotWord());
            TerribleSplit.View lineView = split.view(1);
            TerribleSplit.View wordView = split.view(2);
            LinkedHashMap<String, IntList> map = new LinkedHashMap<>();
            try {
                IntList add = new IntList(0, 0);
                int id = 1;
                while (lineView.hasNext()) {
                    if (wordView.showToken().length() == 0) {
                            wordView.next();
                    }
                    while (wordView.hasNext()) {
                        String word = wordView.next().toLowerCase();
                        IntList idx = map.putIfAbsent(word, add);
                        if (null == idx) {
                            idx = map.get(word);
                            add = new IntList(0, 0);
                        }
                        if (true) {
                            idx.append(id);
                            idx.swap(-2, -1);
                        }
                        idx.set(-1, idx.get(-1) + 1);
                        id++;
                    }
                    for (IntList idx : map.values()) {
                        idx.set(0, idx.get(0) + idx.pop());
                        idx.append(0);
                    }
                    lineView.next();
                }
                // Traverse map and print
                StringBuilder answer = new StringBuilder();
                for (Map.Entry<String, IntList> kv : map.entrySet()) {
                    String word = kv.getKey();
                    answer.setLength(0);
                    answer.append(kv.getKey());
                    answer.append(" ");
                    IntList idx = kv.getValue();
                    int p = idx.size() - 2;
                    for (int i = 0; i <= p; i++) {
                        answer.append(idx.get(i));
                        answer.append((i != p)? " " : System.lineSeparator());
                    }
                    output.write(answer.toString());
                }
            } finally {
                input.close();
                output.close();
                split.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO exception occured " + e.getMessage());
        }
    }
}