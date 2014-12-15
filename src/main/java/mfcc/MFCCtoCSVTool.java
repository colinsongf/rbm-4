package mfcc;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import mfcc.MFCCWalker.FileVisitor;

public class MFCCtoCSVTool {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("");
        }
        try {
            Path path = FileSystems.getDefault().getPath(args[0]);
            new MFCCWalker(new FileVisitor() {
                @Override public void visit(Path mfcc, Path csv) {
                    System.out.println(mfcc);
                    MFCVectorStats stats = new MFCVectorStats();
                    try {
                        new MFCReader(stats).read(mfcc);
                        stats.output(System.out);
                    } catch (IOException e) {
                        System.err.println("problem with " + mfcc + " : " + e);
                        e.printStackTrace();
                    }
                }
            }).walk(path);
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }
}
