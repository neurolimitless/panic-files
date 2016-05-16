package hido.panic;


import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;

public class CipherProcessor {
    public String AES_CFB(String key, String initVector, byte[] data, int mode) {
        try {
            if (mode<1 || mode>2) throw new IllegalArgumentException("Invalid mode. 1-encryption ; 2-decryption");
            //before decryption we must convert hex to binary
            if (mode == 2) data = DatatypeConverter.parseHexBinary(new String(data));
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
            cipher.init(mode, keySpec, iv);
            if (mode == 1) return Hex.encodeHexString(cipher.doFinal(data));
            else return new String(cipher.doFinal(data));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String asciiToHex(String ascii) {
        return String.format("%x", new BigInteger(1, ascii.getBytes())).toUpperCase();
    }
}
