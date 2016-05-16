package hido.panic;

import org.apache.commons.codec.binary.Hex;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileProcessor {

    public final int CORE_COUNT;

    public FileProcessor() {
        CORE_COUNT = Runtime.getRuntime().availableProcessors(); //retrieving how much cores has processor
    }


    public void saveStructure(Structure structure, boolean encoded) {
        try {
            int length = structure.getData().length;
            String hex = Hex.encodeHexString(structure.getData());
            if (encoded) length = hex.length();
            String path = structure.getPath();
            FileChannel channel = new RandomAccessFile(path + "en", "rw").getChannel();
            ByteBuffer writeBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, length);
            if (encoded) writeBuffer.put(hex.getBytes());
            else writeBuffer.put(structure.getData());
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Structure createStructure(String path, boolean encoding) {
        try {
            Structure structure = new Structure();
            FileChannel channel = new RandomAccessFile(path, "r").getChannel();
            ByteBuffer bb = ByteBuffer.allocateDirect(1024* 64 * 1024);
            bb.clear();
            if (channel.size() > Integer.MAX_VALUE) throw new IOException("File is too big");
            byte[] bytes = new byte[(int) channel.size()];
            while (channel.read(bb) > 0) {
                bb.flip();
                for (int i = 0; i < channel.size(); i++) {
                    bytes[i] = bb.asReadOnlyBuffer().get(i);
                    bb.clear();
                }
            }
            if (!encoding) structure.setData(DatatypeConverter.parseHexBinary(new String(bytes)));
            else structure.setData(bytes);
            structure.setPath(path);
            if (path.contains("\\")) structure.setName(path.substring(path.lastIndexOf("\\") + 1));
            else structure.setName(path);
            return structure;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getFilesPaths(String path) {
        List<String> paths = new ArrayList<>();
        try {
            Files.walk(Paths.get(path)).filter(Files::isRegularFile).forEach(path1 -> paths.add(path1.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return paths;
    }


}

