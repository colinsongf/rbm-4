package mfcc;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestMFC {
    public static Path path;

    static {
        loadTestMFC();
    }

    private static void loadTestMFC() {
        try {
            path = Files.createTempFile("mfc_reader", ".mfc");
            Files.copy(openInputStream(),
                       path);

            path.toFile().deleteOnExit();
        } catch (IOException e) {
            throw new RuntimeException("couldn't get test.mfc", e);
        }
    }

    private static InputStream openInputStream() {
        return TestMFC.class.getResourceAsStream("test.mfc");
    }
}
