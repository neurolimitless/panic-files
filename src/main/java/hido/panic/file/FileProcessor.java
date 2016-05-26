package hido.panic.file;

import hido.panic.cipher.CipherMode;
import org.apache.commons.codec.binary.Hex;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FileProcessor {

    private FileProcessor(){}

    public static void saveStructure(Structure structure, CipherMode mode) {
        try {
            byte[] data;
            if (mode == CipherMode.ENCRYPTION) data = Hex.encodeHexString(structure.getData()).getBytes("UTF-8");
            else data = structure.getData();
            if (data!=null) {
                int length = data.length;
                String path = structure.getPath();
                FileChannel channel = new RandomAccessFile(path + "en", "rw").getChannel();
                ByteBuffer writeBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, length);
                writeBuffer.put(data);
                channel.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Structure createStructure(String path) {
        Structure structure = new Structure();
        structure.setName(Paths.get(path).getFileName().toString());
        structure.setPath(path);
        structure.setData(readFileBytes(path));
        return structure;
    }

    private static byte[] readFileBytes(String path){
        try(FileChannel channel = new RandomAccessFile(path, "r").getChannel()) {

            ByteBuffer bb = ByteBuffer.allocateDirect(1024 * 64 * 1024);
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
            e.printStackTrace();
        }
        return new byte[0];
    }

    public static List<String> getFilesPaths(String path) {
        try {
            return Files.walk(Paths.get(path))
                    .filter(Files::isRegularFile)
                    .map(path1 -> path1.toString())
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}

