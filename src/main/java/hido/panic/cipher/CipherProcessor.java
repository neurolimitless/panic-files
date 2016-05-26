package hido.panic.cipher;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CipherProcessor {

    private CipherProcessor(){}

    public static byte[] AES_CFB(String key, String initVector, byte[] data, CipherMode mode) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
            cipher.init(mode.getValue(), keySpec, iv);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().contains("illegal character")) System.out.println("This file isn't encrypted by AES "+key.length()*16+ ".");
            System.exit(-1);
            return null;
        }
    }

}
