package JavaFileTransformation;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static JavaFileTransformation.LookupTableReader.readKeywordLookupTableIntoMap;

public class JavaFileShrinker {
    private File targetFile;
    private final HashMap<String, String> keywordLookup;

    public JavaFileShrinker() {
        this.keywordLookup = readKeywordLookupTableIntoMap("KeywordTable.txt");
    }

    public void setTargetFile(String targetPath) {
        targetFile = new File(targetPath);
    }

    private List<String> parseFileIntoWords() {
        try {
            FileReader reader = new FileReader(targetFile);
            List<String> words = new ArrayList<>();
            StringBuilder word = new StringBuilder();
            int c;
            boolean acceptingSpaces = false;
            while ((c = reader.read()) != -1) {
                char ch = (char) c;
                if (!Character.isAlphabetic(ch)) {
                    if (!word.isEmpty()) {
                        words.add(word.toString());
                        word = new StringBuilder();
                    }
                    if (ch == ' ') {
                        if (acceptingSpaces) {
                            words.add(String.valueOf(ch));
                        }
                    } else if (ch != '\n') {
                        acceptingSpaces = true;
                        words.add(String.valueOf(ch));
                    } else {
                        acceptingSpaces = false;
                    }
                } else {
                    acceptingSpaces = true;
                    word.append(ch);
                }
            }
            reader.close();
            return words;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> handleWordList(List<String> words) {
        List<String> result = new ArrayList<>();
        List<String> uniques = new ArrayList<>();
        for (String s : words) {
            if (s.length() == 1 && !Character.isAlphabetic((s.charAt(0))) && !Character.isDigit(s.charAt(0))) {
                result.add(s);
            } else if (keywordLookup.containsKey(s)) {
                result.add(keywordLookup.get(s));
            } else if (uniques.contains(s)) {
                result.add(uniques.indexOf(s) + "B");
            } else {
                result.add(uniques.size() + "B");
                uniques.add(s);
            }
        }
        result.add("<" + String.join("|", uniques) + ">");
        return result;
    }

    public List<String> shrink() {
        return handleWordList(parseFileIntoWords());
    }
}
