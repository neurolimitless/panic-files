package hido.panic.cipher;

public class CipherFactory {

    public static Cipher factory(CipherType cipherType, String key, String initVector) {
        switch (cipherType) {
            case AES_CFB:
                return new CipherAesCfb(key, initVector);
        }
        return null;
    }

}
