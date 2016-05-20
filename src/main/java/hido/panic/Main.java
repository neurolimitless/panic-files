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
        ThreadProcessor threadProcessor = new ThreadProcessor();
        threadProcessor.execute(paths,mode);
        long finishTime = System.currentTimeMillis();
        System.out.println((finishTime-startTime)/1000 +" s.");
    }
}
