package JavaFileTransformation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;

public class LookupTableReader {
    public static HashMap<String, String> readKeywordLookupTableIntoMap(String lookupTableURL) {
        HashMap<String, String> lookup = new HashMap<>();
        File table = new File(lookupTableURL);
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
            lookup.put(keyword, symbol);
        }
        return lookup;
    }
}
