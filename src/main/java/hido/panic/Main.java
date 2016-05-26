package hido.panic;

import java.io.UnsupportedEncodingException;

public class Main {


    public static void main(String[] args) throws UnsupportedEncodingException {

        args = new String[]{"", "", "1", "C:/jack/"};

        checkParams(args);

        CipherType cipherType = CipherType.AES_CFB;

        //TODO this data should be received from user
        String key = "someKeysomeKey16";
        String initVector = "someKeysomeKey16";

        Cipher cipher = CipherFactory.factory(cipherType, key, initVector);
        cipher.setCipherMode(parseCipherMode(args[2]));

        ThreadsPool threadsPool = new ThreadsPool();
        threadsPool.execute(
        //TODO what are params args[0] and args[1]?
                FileProcessor.getFilesPaths(args[3]),
                cipher
        );
        threadsPool.shutdown();
    }


    private static CipherMode parseCipherMode(String cipherStringParam){
        if (cipherStringParam != null && !cipherStringParam.isEmpty()) {
            return CipherMode.getModeByValue(Integer.parseInt(cipherStringParam));
        }
        return CipherMode.DECRYPTION;// return default
    }


    private static void checkParams(String[] args){
        if(args.length < 2){//TODO how much params should be?
            printHelp();
            throw new IllegalArgumentException("not all params were entered!");
        }
    }

    private static void printHelp(){
        System.out.println("Params description: ");//TODO param description
    }
}
