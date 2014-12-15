package util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EmptyStackException;
import java.util.Stack;

import static java.nio.file.Files.createTempDirectory;

public class TestTempDirectory {
    public static final Charset CHARSET = Charset.forName("UTF-8");

    public static Path path;

    public static Stack<Path> stack = new Stack<>();

    public static void newOne() {
        try {
            path = createTempDirectory("testdir");
        } catch (IOException e) {
            throw new RuntimeException("temp dir problem", e);
        }
    }

    public static void cleanup() {
        try {
            stack.clear();
            if (path == null) {
                return;
            }

            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                        throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override public FileVisitResult postVisitDirectory(Path dir, IOException exc)
                        throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });

            path = null;

        } catch(IOException e) {
            throw new RuntimeException("temp dir problem", e);
        }
    }

    public static Path createDir(String name) {
        try {
            Path dirPath = getCurrentDirAndCreateIt().resolve(name);
            Files.createDirectories(dirPath);
            return dirPath;
        } catch (IOException e) {
            throw new RuntimeException("temp dir problem", e);
        }
    }

    public static Path createFile(String name, String content) {
        return createFile(name, content.getBytes(CHARSET));
    }

    public static Path createFile(String name, byte[] bytes) {
        try {
            Path filePath = getCurrentDirAndCreateIt().resolve(name);
            Files.write(filePath, bytes);
            return filePath;
        } catch (IOException e) {
            throw new RuntimeException("temp dir problem", e);
        }
    }

    public static Path createFile(String name, InputStream in) {
        try {
            Path filePath = getCurrentDirAndCreateIt().resolve(name);
            Files.copy(in, filePath);
            return filePath;
        } catch (IOException e) {
            throw new RuntimeException("temp dir problem", e);
        }
    }

    public static Path getCurrentDir() {
        if (!stack.isEmpty()) {
            return stack.peek();
        }
        return path;
    }

    private static Path getCurrentDirAndCreateIt() {
        Path dir = getCurrentDir();
        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            throw new RuntimeException("temp dir problem", e);
        }
        return dir;
    }

    public static Path pushDir(String name) {
        Path dir = getCurrentDir().resolve(name);
        stack.push(dir);
        return dir;
    }

    public static void popDir(String name) {
        try {
            stack.pop();
        } catch (EmptyStackException e) {
            throw new RuntimeException("temp dir problem", e);
        }
    }

    public static Path bringResource(URL resource) {
        try {
            String prefix = resource.getFile();

            int idx = prefix.lastIndexOf("/");
            if (idx != -1) {
                prefix = prefix.substring(idx + 1);
            }

            String suffix = "";

            idx = prefix.lastIndexOf('.');
            if (idx != -1) {
                suffix = prefix.substring(idx);
                prefix = prefix.substring(0, idx);
            }

            Path dir = getCurrentDirAndCreateIt();
            Path file = Files.createTempFile(dir, prefix, suffix);

            try (InputStream in = resource.openStream()) {
                Files.copy(in, file, StandardCopyOption.REPLACE_EXISTING);
            }

            return file;
        } catch (IOException e) {
            throw new RuntimeException("temp dir problem", e);
        }
    }
}
