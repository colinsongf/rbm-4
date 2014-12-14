package mfcc;

import java.nio.file.Path;

import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.insightfullogic.lambdabehave.JunitSuiteRunner;

import util.TestTempDirectory;

import static com.insightfullogic.lambdabehave.Suite.describe;
import static mfcc.MFCCWalker.FileVisitor;
import static util.TestGenerators.fileNames;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

@RunWith(JunitSuiteRunner.class)
public class MFCCWalkerSpec {{
    describe("MFC walker", it -> {
        it.isSetupWith(TestTempDirectory::newOne);
        it.isConcludedWith(TestTempDirectory::cleanup);

        it.should("make no visits if no files", expect -> {
            FileVisitor visitor = Mockito.mock(FileVisitor.class);

            MFCCWalker walker = new MFCCWalker(visitor);
            walker.walk(TestTempDirectory.path);

            Mockito.verifyNoMoreInteractions(visitor);
        });

        it.requires(10)
          .example(fileNames())
          .toShow("visits find a file on FS", (expect, filename) -> {
              TestTempDirectory.createDir("adir");
              TestTempDirectory.pushDir("adir");
              Path tempFile = TestTempDirectory.createFile(filename, "content");

              FileVisitor visitor = Mockito.mock(FileVisitor.class);

              Mockito.doNothing()
                     .when(visitor)
                     .visit(eq(tempFile), any());


              MFCCWalker walker = new MFCCWalker(visitor);
              walker.walk(TestTempDirectory.path);

              Mockito.verifyNoMoreInteractions(visitor);
          });

        it.requires(10)
          .example(fileNames(),
                   fileNames())
          .toShow("visits find two files on FS", (expect, name1, name2) -> {
              TestTempDirectory.createDir("adir");
              TestTempDirectory.pushDir("adir");

              Path f1 = TestTempDirectory.createFile(name1, "content1");
              Path f2 = TestTempDirectory.createFile(name2, "content2");

              FileVisitor visitor = Mockito.mock(FileVisitor.class);

              Mockito.doNothing()
                     .when(visitor)
                     .visit(eq(f1), any());

              Mockito.doNothing()
                     .when(visitor)
                     .visit(eq(f2), any());

              MFCCWalker walker = new MFCCWalker(visitor);
              walker.walk(TestTempDirectory.path);

              Mockito.verifyNoMoreInteractions(visitor);
          });
    });
}}