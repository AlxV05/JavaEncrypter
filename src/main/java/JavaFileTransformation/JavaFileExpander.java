package JavaFileTransformation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static JavaFileTransformation.LookupTableReader.readKeywordLookupTableIntoMap;

public class JavaFileExpander {
    private File targetFile;
    private HashMap<String, String> keywordLookup;

    public JavaFileExpander() {
        this.keywordLookup = new HashMap<>();
        readKeywordLookupTableIntoMap("KeywordTable.txt");
    }

    public void writeCompressedToFile(String compressed) {
        try {
            FileWriter writer = new FileWriter(targetFile);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> parseCompressedIntoWords(String compressed) {
        List<String> result = new ArrayList<>();
        List<String> uniques = getUniquesFromCompressed(compressed);

        char[] chars = compressed.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            String s = String.valueOf(chars[i]);
            if (keywordLookup.containsValue(s)) {
                result.add(getKeyByValue(keywordLookup, s));
            } else if (uniques.contains(s)) {

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
