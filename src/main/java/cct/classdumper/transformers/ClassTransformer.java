package cct.classdumper.transformers;

import cct.classdumper.display.Prefix;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.List;

/**
 * @author ethanol
 * @since 11/08/23
 *
 * Some ideas and code here are taken from accessmodifier364's Dumper which is why he is credited as an author.
 * https://github.com/accessmodifier364/Dumper
 */

public class ClassTransformer implements ClassFileTransformer {
    public static final String DIRECTORY = System.getenv("USERPROFILE") + "/Desktop/dump/";

    @Override
    public byte[] transform(
            ClassLoader loader,
            String className,
            Class<?> classRedef,
            ProtectionDomain domain,
            byte[] classFileBuffer
    )
    {
        if (dumpCheck(className)) {
            System.out.println(Prefix.getPrefix() + className);
            String name = className + ".class";

            if (name.contains("/")) {
                try {
                    Files.createDirectories(Paths.get(DIRECTORY + "classes/"
                            + name.substring(0, name.lastIndexOf('/'))));
                } catch (IOException e) {
                    System.out.println("Error creating directories: " + e.getMessage());
                }
            }

            try {
                FileOutputStream fos = new FileOutputStream(DIRECTORY + "classes/" + name);
                fos.write(classFileBuffer);
                fos.close();
            } catch (IOException e) {
                System.out.println("Error writing class: " + e.getMessage());
            }
        }
        return classFileBuffer;
    }

    public boolean dumpCheck(String className) {
        if (className != null) {
            return exclusions.stream().noneMatch(
                    exclusion -> className.startsWith(exclusion)
            );
        }
        return true;
    }

    private final List<String> exclusions = Arrays.asList(
            "java", "sun", "javax", "jdk", "net/minecraft",
            "com/sun", "org/spongepowered"
    );
}
