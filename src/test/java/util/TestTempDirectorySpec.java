package util;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

import org.junit.runner.RunWith;

import com.insightfullogic.lambdabehave.JunitSuiteRunner;

import static com.insightfullogic.lambdabehave.Suite.describe;
import static com.insightfullogic.lambdabehave.generators.Generator.asciiStrings;

@RunWith(JunitSuiteRunner.class)
public class TestTempDirectorySpec {{
    describe("test temp directory tool", it -> {
        it.isConcludedWith(TestTempDirectory::cleanup);
        it.should("create a dir on FS", expect -> {
            TestTempDirectory.newOne();
            expect.that(TestTempDirectory.path).isNotNull();
            expect.that(Files.isDirectory(TestTempDirectory.path)).is(true);
        });

        it.requires(5)
          .example(TestGenerators.fileNames())
          .toShow("create sub dir", (expect, name) -> {
              TestTempDirectory.newOne();
              Path dir = TestTempDirectory.createDir(name);
              expect.that(Files.isDirectory(dir)).is(true);
          });

        it.requires(5)
          .example(TestGenerators.fileNames(),
                   asciiStrings())
          .toShow("create file", (expect, name, content) -> {
              TestTempDirectory.newOne();
              Path file = TestTempDirectory.createFile(name, content);
              expect.that(Files.readAllBytes(file))
                    .isEqualTo(content.getBytes("UTF-8"));
          });

        it.requires(5)
          .example(TestGenerators.fileNames(),
                   TestGenerators.fileNames(),
                   asciiStrings())
          .toShow("create file in nested dir", (expect, dirName, fileName, content) -> {
              TestTempDirectory.newOne();
              Path subDir = TestTempDirectory.pushDir(dirName);
              Path file = TestTempDirectory.createFile(fileName, content);

              expect.that(TestTempDirectory.getCurrentDir())
                    .isEqualTo(subDir);

              expect.that(file.getParent().getFileName().toString())
                    .isEqualTo(dirName);

              expect.that(Files.readAllBytes(file))
                    .isEqualTo(content.getBytes("UTF-8"));
          });


        it.should("be able to bring a resource to FS", expect -> {
            TestTempDirectory.newOne();
            URL resource = getClass().getResource("test.txt");
            Path tempFile = TestTempDirectory.bringResource(resource);

            expect.that(Files.readAllLines(tempFile))
                  .isEqualTo(Collections.singletonList("Karamba!"));
        });
    });
}}