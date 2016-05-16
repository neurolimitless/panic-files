package hido.panic;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws UnsupportedEncodingException {
        long startTime = System.currentTimeMillis();
        //1 - encryption , 2 - decryption
        int mode = 2;
        if (args[2].equals("1")) {
            mode = 1;
        }
        FileProcessor fileProcessor = new FileProcessor();
        List<String> paths = fileProcessor.getFilesPaths(args[3]);
        CipherProcessor cipherProcessor = new CipherProcessor();
        for (String path : paths) {
            long time = System.currentTimeMillis();
            Structure currentStructure = fileProcessor.createStructure(path);
            System.out.println("Current file "+currentStructure.getName());
            currentStructure.setData(cipherProcessor.AES_CFB(args[0], args[1], currentStructure.getData(), mode));
           if (currentStructure.getData()!=null) {
               System.out.println("Saving file");
               fileProcessor.saveStructure(currentStructure, mode);
               long iterationTime = System.currentTimeMillis();
               System.out.println((iterationTime - time) / 1000 + " seconds for " + currentStructure.getData().length / 1024 + " kb file.");
           }
        }
        System.out.println((System.currentTimeMillis() - startTime) / 1000 + "s. total.");
    }
}
