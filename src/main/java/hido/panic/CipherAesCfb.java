package hido.panic;

import javax.xml.bind.DatatypeConverter;


public class CipherAesCfb extends Cipher {

    public CipherAesCfb(String key, String initVector) {
        super(key, initVector);
    }

    @Override
    protected byte[] encrypt(Structure structure, String key, String initVector) {
        return CipherProcessor.AES_CFB(key, initVector, structure.getData(), CipherMode.ENCRYPTION);
    }

    @Override
    protected byte[] decrypt(Structure structure, String key, String initVector) {
        byte[] binary = DatatypeConverter.parseHexBinary(new String(structure.getData()));
        return CipherProcessor.AES_CFB(key, initVector, binary, CipherMode.DECRYPTION);

    }
}
