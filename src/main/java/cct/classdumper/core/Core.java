package cct.classdumper.core;

import cct.classdumper.display.Prefix;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ethanol, accessmodifier364
 * @since 11/08/23
 *
 * Some ideas and code here are taken from accessmodifier364's Dumper which is why he is credited as an author.
 * https://github.com/accessmodifier364/Dumper
 */

public class Core {
    /**
     * The directory containing the class files to be dumped.
     */
    public static final String DIRECTORY = System.getenv("USERPROFILE") + "/Desktop/dump/classes/";

    /**
     * The directory where the JAR file will be created.
     */
    public static final String DUMP_DIRECTORY = System.getenv("USERPROFILE") + "/Desktop/dump/";

    /**
     * The name of the JAR file to be created.
     */
    public static final String JAR_FILE_NAME = "dumped.jar";

    public static void main(String[] args) throws IOException {
        try (JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(DUMP_DIRECTORY + JAR_FILE_NAME))) {
            List<File> classFiles = getFilesInDirectory(DIRECTORY, ".class");

            for (File classFile : classFiles) {
                String entryName = classFile.toPath().toString().replace(DIRECTORY, "");
                System.out.println(Prefix.getPrefix() + "Adding class " + entryName);
                JarEntry jarEntry = new JarEntry(entryName);
                jarOutputStream.putNextEntry(jarEntry);
                Files.copy(classFile.toPath(), jarOutputStream);
                jarOutputStream.closeEntry();
            }
        }
        System.out.println("[CCT] JAR file created successfully.");
    }

    /**
     * Get a list of files with a specified extension in a directory.
     *
     * @param dir      The directory to search.
     * @param extension The file extension to filter by.
     * @return A list of files with the specified extension.
     * @throws IOException If an I/O error occurs.
     */
    private static List<File> getFilesInDirectory(String dir, String extension) throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(dir))) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(extension))
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        }
    }
}
