package JavaFileTransformation;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JavaFileShrinker {
    private File targetFile;
    private HashMap<String, String> keywordLookup;

    public JavaFileShrinker() {
        this.keywordLookup = new HashMap<>();
        readKeywordLookupTableIntoMap();
    }

    private void readKeywordLookupTableIntoMap() {
        File table = new File("KeywordTable.txt");
        List<String> lines;
        try {
            lines = Files.readAllLines(table.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (String s : lines) {
            StringBuilder accumulator = new StringBuilder();
            String keyword = null;
            String symbol = null;
            boolean keywordFound = false;
            for (char c : s.toCharArray()) {
                if (Character.isAlphabetic(c)) {
                    if (keywordFound) {
                        symbol = String.valueOf(c);
                        break;
                    } else {
                        accumulator.append(c);
                    }
                } else {
                    keyword = accumulator.toString();
                    keywordFound = true;
                }
            }
            keywordLookup.put(keyword, symbol);
        }
    }

    public boolean setTargetFile(String targetURL) {
        if (Files.exists(Path.of(targetURL))) {
            targetFile = new File(targetURL);
            return true;
        }
        return false;
    }

    private List<String> parseFileIntoWords() {
        try {
            FileReader reader = new FileReader(targetFile);
            List<String> words = new ArrayList<>();
            StringBuilder word = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                char ch = (char) c;
                if (!Character.isAlphabetic(ch)) {
                    if (!word.isEmpty()) {
                        words.add(word.toString());
                        word = new StringBuilder();
                    }
                    if (ch != ' ' && ch != '\n') {
                        words.add(String.valueOf(ch));
                    }
                } else {
                    word.append(ch);
                }
            }
            return words;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> handleWordList(List<String> words) {
        List<String> result = new ArrayList<>();
        List<String> uniques = new ArrayList<>();
        for (String s : words) {
            if (s.length() == 1) {
                result.add(s);
            } else if (keywordLookup.containsKey(s)) {
                result.add(keywordLookup.get(s));
            } else if (uniques.contains(s)) {
                result.add(String.valueOf(uniques.indexOf(s)));
            } else {
                result.add(String.valueOf(uniques.size()));
                uniques.add(s);
            }
        }
        result.add("<" + String.join("|", uniques) + ">");
        return result;
    }

    public static void main(String[] args) {
        JavaFileShrinker shrinker = new JavaFileShrinker();
        shrinker.setTargetFile("src/main/java/JavaFileTransformation/JavaFileShrinker.java");
        List<String> s = shrinker.handleWordList(shrinker.parseFileIntoWords());
        System.out.println(String.join("", s));
    }
}
