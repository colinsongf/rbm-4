package mfcc;

import java.net.URL;
import java.nio.file.Path;

import org.junit.runner.RunWith;

import com.insightfullogic.lambdabehave.JunitSuiteRunner;

import util.TestTempDirectory;

import static com.insightfullogic.lambdabehave.Suite.describe;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyShort;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(JunitSuiteRunner.class)
public class MFCReaderSpec {{
    URL testMFC = getClass().getResource("test.mfc");

    describe("mfc coefficient reader", it -> {

        it.isSetupWith(TestTempDirectory::newOne);
        it.isConcludedWith(TestTempDirectory::cleanup);

        it.should("read some amount of coefficient from file", expect -> {
            MFCVectorVisitor vectorVisitor = mock(MFCVectorVisitor.class);
            Path path = TestTempDirectory.bringResource(testMFC);
            new CoeffReader(vectorVisitor).read(path);

            verify(vectorVisitor, times(1))
                    .count(eq(189), anyInt(), anyShort(), anyShort());


            verify(vectorVisitor, times(189))
                    .coefficients(any(float[].class));

            verify(vectorVisitor, times(1))
                    .theEnd(anyShort());

            verifyNoMoreInteractions(vectorVisitor);
        });

    });
}}
