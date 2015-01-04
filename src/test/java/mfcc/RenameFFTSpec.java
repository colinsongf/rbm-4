package mfcc;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.junit.runner.RunWith;

import com.insightfullogic.lambdabehave.JunitSuiteRunner;

import static com.insightfullogic.lambdabehave.Suite.describe;

@RunWith(JunitSuiteRunner.class)
public class RenameFFTSpec {{

    describe("RenameFFT", it -> {
        it.should("rename uniformly", expect -> {
            RenameFFT renameFFT = new RenameFFT();
            File file = new File("A33/a33-1562010_0057.fft");
            File newF = renameFFT.newFileName(file);

            expect.that(newF).isEqualTo(new File("a33/00/a33-0001562010_0057.fft"));
        });

        it.should("look everywhere", expect -> {
            Files.walk(Paths.get("/home/oleksiyp/deeplearn/fft2")).forEach(path -> {
                if (!Files.isRegularFile(path)) {
                    return;
                }
                RenameFFT renameFFT = new RenameFFT();
                renameFFT.newFileName(path.toFile());
            });
        });

        it.should("look everywhere and has len 30", expect -> {
            Files.walk(Paths.get("/home/oleksiyp/deeplearn/fft2")).forEach(path -> {
                if (!Files.isRegularFile(path)) {
                    return;
                }
                RenameFFT renameFFT = new RenameFFT();
                File val = renameFFT.newFileName(path.toFile());
                if (val.toString().length() != 30) {
                    expect.failure("no len 30: " + val + ", len=" + val.toString().length());
                }
            });
        });

        it.should("has no clashes", expect -> {
            Set<String> set = new HashSet<>();
            Files.walk(Paths.get("/home/oleksiyp/deeplearn/fft2")).forEach(path -> {
                if (!Files.isRegularFile(path)) {
                    return;
                }
                RenameFFT renameFFT = new RenameFFT();
                File val = renameFFT.newFileName(path.toFile());

                if (!set.add(val.toString())) {
                    expect.failure(val.toString());
                }
            });
        });

    });
}
}
