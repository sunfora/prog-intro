package md2html;

import java.io.*;
import java.util.*;

public class Md2Html {
    public static void main(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("Need input and output file path...");
        }
        String input = args[0];
        String output = args[1];
        StringBuilder result = new StringBuilder();
        try {
            Parser parser = new Parser(new InputStreamReader(new FileInputStream(input), "utf8"));
            try {
                while (parser.hasNext()) {
                    HTMLable t = parser.next();
                    t.toHTML(result);
                    result.append(System.lineSeparator());
                }
            } catch (IOException e) {
                System.out.println("IOException occured : " + e.getMessage());
                return;
            } finally {
                parser.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Input file not found : " + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            System.out.println("My bad ... " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Probably something bad is happening " + e.getMessage());
        }
        try {
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(output), "utf8");
            try {
                out.write(result.toString());
            } catch (IOException e) {
                System.out.println("IOException occured : " + e.getMessage());
                return;
            } finally {
                out.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Output file not found : " + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            System.out.println("My baad..." + e.getMessage());
        } catch (IOException e) {
            System.out.println("Probably something bad is happening " + e.getMessage());
        }
    }
}