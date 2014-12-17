package mfcc;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;

import mfcc.MFCCWalker.FileVisitor;

public class MFCCtoCSVTool {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("");
        }
        try {
            FileSystem fs = FileSystems.getDefault();
            Path path = fs.getPath(args[0]);
            new MFCCWalker(new FileVisitor() {
                @Override public void visit(Path mfcc, Path csv) {
                    if (Files.exists(csv) && !checkOutdated(mfcc, csv)) {
                        System.out.print('s');
                        return;
                    }
                    System.out.print('.');
                    CoeffStats stats = new CoeffStats();
                    try (PrintWriter writer = new PrintWriter(csv.toFile())) {
                        new MFCIn(stats).read(mfcc);
                        CSVOut csvOut = new CSVOut(writer);
                        Norm normedCsvOut = new Norm(stats, csvOut);
                        new MFCIn(normedCsvOut).read(mfcc);
                    } catch (IOException e) {
                        System.err.println("problem with " + mfcc + " : " + e);
                        e.printStackTrace();
                    }
                }
            }).walk(path);
            System.out.println();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    private static boolean checkOutdated(Path mfcc, Path csv) {
        try {
            FileTime csvTime = Files.getLastModifiedTime(csv);
            FileTime mfccTime = Files.getLastModifiedTime(mfcc);
            if (csvTime.compareTo(mfccTime) < 0 || Files.size(csv) == 0) {
                return true;
            }
        } catch (IOException e) {
        }
        return false;
    }
}
