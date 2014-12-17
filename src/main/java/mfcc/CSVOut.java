package mfcc;

import java.io.PrintWriter;

public class CSVOut implements CoeffVisitor {
    private PrintWriter writer;

    public CSVOut(PrintWriter writer) {
        this.writer = writer;
    }

    @Override
    public void coefficients(float[] vector) {
        boolean first = true;
        for (float val : vector) {
            if (!first) {
                writer.print(',');
            }
            first = false;
            writer.print(val);
        }
        writer.println();
    }

    @Override
    public void theEnd(short checksum) {
        writer.close();
    }
}
