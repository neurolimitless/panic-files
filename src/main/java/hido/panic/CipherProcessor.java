package hido.panic;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;

public class CipherProcessor {
    public byte[] AES_CFB(String key, String initVector, byte[] data, int mode) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
            cipher.init(mode, keySpec, iv);

            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String asciiToHex(String ascii) {
        return String.format("%x", new BigInteger(1, ascii.getBytes())).toUpperCase();
    }
}
