package PNGImageTransformation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

public class PNGByteExtractor {
    private File targetFile;

    public void setTargetFile(String path) {
        this.targetFile = new File(path);
    }

    public byte[] extractBytesFromFile(byte[] delimiter) {
        try {
            byte[] bytes = Files.readAllBytes(targetFile.toPath());
            int index = indexOf(bytes, delimiter);
            return Arrays.copyOfRange(bytes, index, bytes.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int indexOf(byte[] array, byte[] target) {
        if (target.length == 0) {
            return 0;
        }
        outer:
        for (int i = 0; i <= array.length - target.length; i++) {
            for (int j = 0; j < target.length; j++) {
                if (array[i+j] != target[j]) {
                    continue outer;
                }
            }
            return i;
        }
        return -1;
    }
}
