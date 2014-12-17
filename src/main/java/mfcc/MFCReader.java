package mfcc;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;

public class MFCReader {
    private MFCVectorVisitor vectorVisitor;

    private byte[] bytes;
    private ByteBuffer buf;

    public MFCReader(MFCVectorVisitor vectorVisitor) {
        this.vectorVisitor = vectorVisitor;
    }

    public void read(Path path) throws IOException {
        DataInputStream in = new DataInputStream(Files.newInputStream(path));
        int nSamples = in.readInt();
        int samplePeriod = in.readInt();
        short sampleSize = in.readShort();
        short sampleKind = in.readShort();

        vectorVisitor.count(nSamples, samplePeriod, sampleSize, sampleKind);

        bytes = new byte[sampleSize];
        buf = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN);

        float []coefficients = new float[sampleSize / 4];
        for (int i = 0; i < nSamples; i++) {
            readThemAll(in, coefficients);
            vectorVisitor.coefficients(coefficients);
        }

        short checksum = in.readShort();
        vectorVisitor.theEnd(checksum);

        bytes = null;
        buf = null;
    }

    private void readThemAll(DataInputStream in, float[] coefficients)
            throws IOException {

        in.readFully(bytes);
        buf.rewind();

        for (int j = 0; j < coefficients.length; j++) {
            coefficients[j] = buf.getFloat();
        }
    }

}
