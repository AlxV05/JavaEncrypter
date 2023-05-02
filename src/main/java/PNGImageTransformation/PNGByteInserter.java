package PNGImageTransformation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class PNGByteInserter {
    private File targetFile;

    public void setTargetFile(String path) {
        this.targetFile = new File(path);
    }

    public void appendBytesToFile(byte[] delimiter, byte[] data) {
        try {
            Files.write(targetFile.toPath(), delimiter, StandardOpenOption.APPEND);
            Files.write(targetFile.toPath(), data, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
