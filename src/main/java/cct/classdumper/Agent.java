package cct.classdumper;

import cct.classdumper.transformers.ClassTransformer;

import java.lang.instrument.Instrumentation;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author ethanol, accessmodifier364
 * @since 11/08/23
 *
 * Some ideas and code here are taken from accessmodifier364's Dumper which is why he is credited as an author.
 * https://github.com/accessmodifier364/Dumper
 */

public class Agent {
    public static final String dumpDirectory = System.getenv("USERPROFILE") + "/Desktop/dump/";

    public static void premain(String args[], Instrumentation instrumentation) {
        try {
            Path dumpedClassesDirectory = Paths.get(dumpDirectory, "dumpedClasses");
            Files.createDirectories(dumpedClassesDirectory);
        } catch (Exception e) {
            System.err.println("Error creating directories: " + e.getMessage());
        }

        // Add the transformer
        instrumentation.addTransformer(new ClassTransformer());
    }
}
