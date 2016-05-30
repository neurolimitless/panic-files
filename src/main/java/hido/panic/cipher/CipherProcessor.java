package hido.panic.cipher;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;

public class CipherProcessor {

    private static final Logger log = LogManager.getLogger("Cipher logger");

    private CipherProcessor() {
    }

    public static byte[] AES_CFB(String key, String initVector, byte[] data, CipherMode mode) {
        try {
            byte[] keyByte = convert(key);
            byte[] ivByte = convert(initVector);
            IvParameterSpec iv = new IvParameterSpec(ivByte);
            SecretKeySpec keySpec = new SecretKeySpec(keyByte, "AES");
            Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
            cipher.init(mode.getValue(), keySpec, iv);
            return cipher.doFinal(data);
        } catch (Exception e) {
            log.warn(e.getMessage());
            if (e.getMessage().contains("illegal character"))
                log.warn("This file isn't encrypted by AES " + key.length() * 16 + ".");
            System.exit(-1);
            return null;
        }
    }

    private static byte[] convert(String s) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        if (s.length() > 16) result.append(s.substring(0, 16));
        else if (s.length() < 16) {
            result.append(s);
            for (int i = 0; i < 16 - s.length(); i++) {
                result.append("0");
            }
        }
        return result.toString().getBytes("UTF-8");
    }

}
