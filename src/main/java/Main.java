import JavaFileTransformation.JavaFileExpander;
import JavaFileTransformation.JavaFileShrinker;

public class Main {
    public static void main(String[] args) {
        JavaFileShrinker shrinker = new JavaFileShrinker();
        JavaFileExpander expander = new JavaFileExpander();
        shrinker.setTargetFile("src/main/java/JavaFileTransformation/JavaFileShrinker.java");
        System.out.println(String.join("", shrinker.shrink()));
        expander.setTargetFile("Output.java");
        expander.writeCompressedToFile(String.join("", shrinker.shrink()));
    }
}
