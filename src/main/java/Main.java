import JavaFileTransformation.JavaFileExpander;
import JavaFileTransformation.JavaFileShrinker;
import PNGImageTransformation.PNGByteExtractor;
import PNGImageTransformation.PNGByteInserter;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
//        JavaFileShrinker shrinker = new JavaFileShrinker();
        JavaFileExpander expander = new JavaFileExpander();

//        shrinker.setTargetFile("src/main/java/JavaFileTransformation/JavaFileShrinker.java");
        expander.setTargetFile("Output.java");

//        PNGByteInserter inserter = new PNGByteInserter();
        PNGByteExtractor extractor = new PNGByteExtractor();
//        byte[] data = String.join("", shrinker.shrink()).getBytes();
        byte[] delim = "%@%!)(%&)(#".getBytes();

//        inserter.setTargetFile("Down.png");
        extractor.setTargetFile("/home/alxv05/Down.png");

//        inserter.appendBytesToFile(delim, data);

        String extracted = new String(extractor.extractBytesFromFile(delim)).substring(delim.length);

        expander.writeCompressedToFile(extracted);
    }
}
