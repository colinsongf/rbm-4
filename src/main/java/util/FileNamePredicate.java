package util;

import java.util.function.Predicate;

public class FileNamePredicate implements Predicate<String> {
    public static final Predicate<String> INSTANCE = new FileNamePredicate();

    FileNamePredicate() {

    }

    private static final char[] ILLEGAL_CHARACTERS = {'/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':' };

    @Override
    public boolean test(String s) {
        if (s.isEmpty() || s.equals(".") || s.equals("..")) {
            return false;
        }

        for (char c : ILLEGAL_CHARACTERS) {
            if (s.indexOf(c) != -1) {
                return false;
            }
        }
        return true;
    }
}
