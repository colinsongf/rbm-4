package mfcc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RenameFFT {

    public static final Pattern FNAME_PATTERN = Pattern.compile("[aA]-?(\\d\\d)?-([\\d-]{1,20})_(\\d{1,5})(\\d{2}).([a-zA-Z]{3})");

    public File newFileName(File file) {
        String name = file.getName();
        Matcher matcher = FNAME_PATTERN.matcher(name);
        if (matcher.matches()) {
            String n1 = matcher.group(1);
            if (n1 == null || n1.isEmpty()) {
                n1 = "00";
            }
            String n2 = matcher.group(2);
            String n3 = matcher.group(3);
            String n4 = matcher.group(4);
            String n5 = matcher.group(5).toLowerCase();

            while (n2.length() < 10) {
                n2 = "0" + n2;
            }
            n2 = n2.substring(0, 10);

            return new File(new File("a" + n1, n3),
                            "a" + n1 + "-" + n2 + "_" + n3 + n4 + "." + n5);
        }
        throw new RuntimeException("pattern '" + FNAME_PATTERN +
                                   "' not matched for '" + name + "'");
    }

    public static void main(String[] args) throws IOException {
        Files.walk(Paths.get("/home/oleksiyp/deeplearn/wavs2")).forEach(path -> {
            if (!Files.isRegularFile(path)) {
                return;
            }
            if (!path.endsWith("mfc")) {
                return;
            }
            RenameFFT renameFFT = new RenameFFT();
            File f = path.toFile();
            File nf = renameFFT.newFileName(f);
            nf = new File("/home/oleksiyp/deeplearn/mfc/" + nf.toString());

            nf.getParentFile().mkdirs();
            f.renameTo(nf);
            System.out.println(nf);
        });
    }
}
