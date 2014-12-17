package mfcc;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class MFCCWalker {
    private FileVisitor visitor;

    public MFCCWalker(FileVisitor visitor) {
        this.visitor = visitor;
    }

    public interface FileVisitor {
        void visit(Path mfcc, Path csv);
    }

    public void walk(Path path) throws IOException {
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                String filename = file.getFileName().toString();
                if (filename.endsWith(".mfcc") || filename.endsWith(".mfc")) {
                    Path parent = file.getParent();
                    String newName = filename.replaceFirst("\\.mfcc?$", ".csv");
                    Path newFile = parent.resolve(newName);
                    visitor.visit(file, newFile);
                }
                return FileVisitResult.CONTINUE;
            }
        });

    }
}
