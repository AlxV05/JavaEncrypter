package JavaFileTransformation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static JavaFileTransformation.LookupTableReader.readKeywordLookupTableIntoMap;

public class JavaFileExpander {
    private File targetFile;
    private final HashMap<String, String> keywordLookup;

    public JavaFileExpander() {
        this.keywordLookup = readKeywordLookupTableIntoMap("KeywordTable.txt");
    }

    public void setTargetFile(String path) {
        targetFile = new File(path);
    }

    public void writeCompressedToFile(String compressed) {
        try {
            FileWriter writer = new FileWriter(targetFile);
            for (String s : parseCompressedIntoWords(compressed)) {
                writer.write(s);
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> parseCompressedIntoWords(String compressed) {
        List<String> result = new ArrayList<>();
        List<String> uniques = getUniquesFromCompressed(compressed);
        char[] chars = compressed.toCharArray();
        int braceLayer = 0;
        for (int i = 0; i < compressed.lastIndexOf('<'); i++) {
            String s = String.valueOf(chars[i]);
            if (keywordLookup.containsValue(s)) {
                result.add(getKeyByValue(keywordLookup, s));
            } else if (Character.isDigit(chars[i])) {
                StringBuilder b = new StringBuilder();
                b.append(chars[i]);
                int j = i + 1;
                while (chars[j] != 'B') {
                    b.append(chars[j]);
                    j++;
                }
                i += b.length();
                result.add(uniques.get(Integer.parseInt(b.toString())));
            } else if (s.equals("{")) {
                braceLayer++;
                result.add(s + "\n" + "\t".repeat(braceLayer));
            } else if (s.equals("}")) {
                braceLayer--;
                result.add("\n" + "\t".repeat(braceLayer) + s + "\n" + "\t".repeat(braceLayer));
            } else if (s.equals(";")) {
                result.add(s + "\n" + "\t".repeat(braceLayer));
            } else if (!s.equals("B")){
                result.add(s);
            }
        }
        return result;
    }

    private List<String> getUniquesFromCompressed(String compressed) {
        int startIndex = compressed.lastIndexOf('<');
        int endIndex = compressed.lastIndexOf('>');
        String[] strings = compressed.substring(startIndex + 1, endIndex).split("\\|");
        return Arrays.asList(strings);
    }

    private <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}
