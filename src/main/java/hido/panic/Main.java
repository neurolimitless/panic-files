package hido.panic;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        boolean encryption = false;
        int mode = 2;
        if (args[2].equals("1")) {
            encryption = true;
            mode = 1;
        }
        FileProcessor fileProcessor = new FileProcessor();
        List<String> paths = fileProcessor.getFilesPaths("C:\\jack\\");
        CipherProcessor cipherProcessor = new CipherProcessor();
        for (String path : paths) {
            Structure currentStructure = fileProcessor.createStructure(path, encryption);
            currentStructure.setData(cipherProcessor.AES_CFB(args[0], args[1], currentStructure.getData(), mode));
            fileProcessor.saveStructure(currentStructure, encryption);
        }
    }
}
