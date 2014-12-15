package mfcc;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MFCReader {
    public static final int SKIP_BYTES_AFTER_COUNT = 10;
    private MFCVectorVisitor vectorVisitor;

    public MFCReader(MFCVectorVisitor vectorVisitor) {
        this.vectorVisitor = vectorVisitor;
    }

    public void read(Path path) throws IOException {
        DataInputStream in = new DataInputStream(Files.newInputStream(path));
        int cnt = in.readInt();
        in.skipBytes(SKIP_BYTES_AFTER_COUNT);

        vectorVisitor.count(cnt);

        float []coefficients = new float[MFC.N_COEFFICIENTS];
        for (int i = 0; i < cnt; i++) {
            readThemAll(in, coefficients);
            vectorVisitor.coefficients(coefficients);
        }

        vectorVisitor.theEnd();
    }

    private void readThemAll(DataInputStream in, float[] coefficients)
            throws IOException {
        for (int j = 0; j < coefficients.length; j++) {
            coefficients[j] = in.readFloat();
        }
    }

}
