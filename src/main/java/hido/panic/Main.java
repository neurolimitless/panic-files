package hido.panic;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        int mode = 2;
        if (args[2].equals("1")) {
            mode = 1;
        }
        FileProcessor fileProcessor = new FileProcessor();
        List<String> paths = fileProcessor.getFilesPaths("C:\\jack\\");
        CipherProcessor cipherProcessor = new CipherProcessor();
        for (String path : paths) {
            long time = System.currentTimeMillis();
            Structure currentStructure = fileProcessor.createStructure(path);
            currentStructure.setData(cipherProcessor.AES_CFB(args[0], args[1], currentStructure.getData(), mode).getBytes());
            fileProcessor.saveStructure(currentStructure);
            long iterationTime = System.currentTimeMillis();
            System.out.println(iterationTime - time +" for "+currentStructure.getData().length/1024/2+" kb file.");
        }
        System.out.println((System.currentTimeMillis()-startTime)/1000+"s total.");
    }
}
