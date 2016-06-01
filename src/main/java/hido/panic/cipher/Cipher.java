package hido.panic.cipher;

import hido.panic.file.FileProcessor;
import hido.panic.file.Structure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public abstract class Cipher {

    protected static final Logger log = LogManager.getLogger("Cipher logger");
    private String key;
    private String initVector;
    private CipherMode cipherMode;

    public Cipher(String key, String initVector) {
        this.key = key;
        this.initVector = initVector;
    }

    public void launch(File file) {
        Structure structure = FileProcessor.createStructure(file);
        structure.setData(getNewStructureData(structure, cipherMode));
        if (FileProcessor.saveStructure(structure, cipherMode)) log.info(file + " " + cipherMode + " is finished.");
        else log.info(file + " " + cipherMode + " wasn't finished.");
    }

    private byte[] getNewStructureData(Structure structure, CipherMode mode) {
        switch (mode) {
            case ENCRYPTION:
                return encrypt(structure, key, initVector);
            case DECRYPTION:
                return decrypt(structure, key, initVector);
            default:
                return structure.getData();
        }
    }

    public CipherMode getCipherMode() {
        return cipherMode;
    }

    public void setCipherMode(CipherMode cipherMode) {
        this.cipherMode = cipherMode;
    }

    protected abstract byte[] encrypt(Structure structure, String key, String initVector);

    protected abstract byte[] decrypt(Structure structure, String key, String initVector);
}
