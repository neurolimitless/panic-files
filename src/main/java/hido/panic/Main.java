package hido.panic;

import hido.panic.cipher.Cipher;
import hido.panic.cipher.CipherFactory;
import hido.panic.cipher.CipherMode;
import hido.panic.cipher.CipherType;
import hido.panic.file.FileProcessor;
import hido.panic.threads.listeners.WorkCompleteListener;
import hido.panic.threads.runnable.CipherExecutor;
import hido.panic.threads.runnable.CipherRunnable;
import hido.panic.threads.runnable.HierarchyWalker;
import hido.panic.threads.runnable.ThreadPool;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {


    public static void main(String[] args) throws UnsupportedEncodingException {

        checkParams(args);

        CipherType cipherType = CipherType.parseCipherType(args[4]);
        if (cipherType != null) {
            String key = args[0];
            String initVector = args[1];
            Cipher cipher = CipherFactory.factory(cipherType, key, initVector);
            cipher.setCipherMode(parseCipherMode(args[2]));
            List<String> fileList;

            List<CipherRunnable> cipherRunnables = new ArrayList<>();
            if (readFromFile(args[3])){
                fileList = FileProcessor.getFilesPathsFromFile(args[3]);
                for (String file : fileList) {
                    cipherRunnables.add(new CipherExecutor(cipher, new File(file)));
                }
            } else{
                cipherRunnables.add(new HierarchyWalker(Arrays.asList(args[3]), cipher));
            }
            ThreadPool.getInstance().registerThreads(cipherRunnables);
            ThreadPool.getInstance().registerWorkCompleteListener(new WorkCompleteListener() {
                @Override
                public void actionPerformed() {
                    ThreadPool.getInstance().shutdown();
                }
            });
        }
    }

    private static boolean readFromFile(String arg) {
        File file = new File(arg);
        return !file.isDirectory() && file.exists() && file.isFile();
    }

    public static CipherMode parseCipherMode(String cipherStringParam) {
        if (cipherStringParam != null && !cipherStringParam.isEmpty()) {
            return CipherMode.getModeByValue(Integer.parseInt(cipherStringParam));
        }
        return CipherMode.DECRYPTION; //default value
    }

    private static void checkParams(String[] args) {
        if (args.length < 5) {
            printHelp();
            throw new IllegalArgumentException("not all params were entered!");
        }
    }

    private static void printHelp() {
        System.out.println("Params description: ");
        System.out.println("key - must be 16 char (any string)");
        System.out.println("initVector - must be 16 char (any string)");
        System.out.println("mode - 1 for encryption \\ 2 for decryption");
        System.out.println("path - path to directory ");
        System.out.println("cipher - AES_CFB");
        System.out.println("Example:");
        System.out.println("abc123def456ghk7 superINITvector1 1 C:\\filesToEncryption\\ AES_CFB");
    }
}
