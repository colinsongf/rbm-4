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
//                    new MFCReader(new CSVWriter(csv)).read(mfcc);
                }
            }).walk(path);
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }
}
