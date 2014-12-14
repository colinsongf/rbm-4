package util;

import com.insightfullogic.lambdabehave.generators.Generator;

import static com.insightfullogic.lambdabehave.generators.Generator.asciiStrings;

public class TestGenerators {
    public static Generator<String> fileNames() {
        return asciiStrings().matching(FileNamePredicate.INSTANCE);
    }
}
