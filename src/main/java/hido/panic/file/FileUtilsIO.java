package hido.panic.file;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sgnatiuk on 5/31/16.
 */
public class FileUtilsIO {
    public static List<String> readFile(File file) throws IOException {
        return readFile(file, false);
    }

    public static List<String> readFile(File file, boolean createIfNotExists) throws IOException {
        if(createIfNotExists && !file.exists()){
            file.createNewFile();
        }
        return Files.lines(Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8).collect(Collectors.toList());
    }

    public static void writeToFile(File file, Collection<String> rows, boolean override) throws IOException {

        try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, !override)))){
            for (String row : rows) {
                bw.write(row);
                bw.newLine();
            }
        }
    }

    public static void writeToFile(File file, Collection<String> rows) throws IOException {
        writeToFile(file, rows, true);
    }
}
