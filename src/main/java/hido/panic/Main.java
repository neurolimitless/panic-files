package hido.panic;

import hido.panic.cipher.Cipher;
import hido.panic.cipher.CipherFactory;
import hido.panic.cipher.CipherMode;
import hido.panic.cipher.CipherType;
import hido.panic.file.FileProcessor;
import hido.panic.file.ThreadsPool;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class Main {


    public static void main(String[] args) throws UnsupportedEncodingException {

        args = new String[]{"someKeysomeKey16", "someKeysomeKey16", "2", "C:/jack/list.txt", "AES_CFB"};

        checkParams(args);

        CipherType cipherType = parseCipherType(args[4]);
        if (cipherType != null) {
            //TODO this data should be received from user
            String key = args[0];
            String initVector = args[1];
            Cipher cipher = CipherFactory.factory(cipherType, key, initVector);
            cipher.setCipherMode(parseCipherMode(args[2]));
            ThreadsPool threadsPool = new ThreadsPool();
            List<String> fileList;
            if (readFromFile(args[3])) fileList = FileProcessor.getFilesPathsFromFile(args[3]);
            else fileList = FileProcessor.getFilesPaths(args[3]);
            threadsPool.execute(
                    fileList,
                    cipher
            );
            threadsPool.shutdown();
        }
    }

    private static boolean readFromFile(String arg) {
        File file = new File(arg);
        return !file.isDirectory() && file.exists() && file.isFile();
    }

    private static CipherType parseCipherType(String cipherTypeParam) {
        try {
            if (cipherTypeParam != null && !cipherTypeParam.isEmpty()) {
                return CipherType.valueOf(cipherTypeParam);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Wrong cipher algorithm.");
        }
        return null;
    }

    private static CipherMode parseCipherMode(String cipherStringParam) {
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
