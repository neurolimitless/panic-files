package hido.panic.file;

import hido.panic.cipher.CipherMode;
import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FileProcessor {

    private static final Logger log = LogManager.getLogger("Exception logger");

    private FileProcessor() {
    }

    public static boolean saveStructure(Structure structure, CipherMode mode) {
        try {
            byte[] data;
            if (mode == CipherMode.ENCRYPTION) data = Hex.encodeHexString(structure.getData()).getBytes("UTF-8");
            else data = structure.getData();
            if (data != null && data.length!=0) {
                String path = structure.getPath();
                FileChannel channel = new FileOutputStream(path, false).getChannel();
                ByteBuffer writeBuffer = ByteBuffer.wrap(data);
                channel.write(writeBuffer);
                channel.close();
                return true;
            }
        } catch (IOException e) {
            log.error("File saving exception: "+e.getMessage());
        }
        return false;
    }

    public static Structure createStructure(String path) {
        Structure structure = new Structure();
        structure.setName(Paths.get(path).getFileName().toString());
        structure.setPath(path);
        structure.setData(readFileBytes(path));
        return structure;
    }

    private static byte[] readFileBytes(String path) {
        try (FileChannel channel = new RandomAccessFile(path, "r").getChannel()) {
            ByteBuffer bb = ByteBuffer.allocateDirect((int) new File(path).length());
            bb.clear();
            if (channel.size() > Integer.MAX_VALUE) throw new IOException("File is too big.");
            byte[] bytes = new byte[(int) channel.size()];
            while (channel.read(bb) > 0) {
                bb.flip();
                for (int i = 0; i < channel.size(); i++) {
                    bytes[i] = bb.asReadOnlyBuffer().get(i);
                    bb.clear();
                }
            }
            return bytes;
        } catch (IOException e) {
           log.error("File reading exception: "+e.getMessage());
        }
        return new byte[0];
    }

    public static List<String> getFilesPathsFromFile(String fileName) {
        List<String> files = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
            String line;
            File file;
            while (true) {
                line = reader.readLine();
                if (line == null) break;
                file = new File(line);
                if (file.exists() && file.isDirectory()) {
                    files.addAll(getFilesPaths(line));
                } else if (!file.isDirectory()) files.add(line);
                else System.out.println(line + " doesn't exist.");
            }
            reader.close();
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
        return files;
    }

    public static List<String> getFilesPaths(String path) {
        try {
            return Files.walk(Paths.get(path))
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return Collections.emptyList();
    }
}

