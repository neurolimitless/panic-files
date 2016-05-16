package hido.panic;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class CipherProcessor {
    public byte[] AES_CFB(String key, String initVector, byte[] data, int mode) {
        try {
            if (mode < 1 || mode > 2) throw new IllegalArgumentException("Invalid mode. 1-encryption ; 2-decryption.");
            //before decryption we must convert hex to binary
            if (mode == 2) data = DatatypeConverter.parseHexBinary(new String(data));
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
            cipher.init(mode, keySpec, iv);
            return cipher.doFinal(data);
        } catch (Exception e) {
            if (e.getMessage().contains("illegal character")) System.out.println("This file isn't encrypted by AES"+key.length()*16+".");
            return null;
        }

    }

}
