package hido.panic;

public abstract class Cipher {

    private String key;
    private String initVector;
    private CipherMode cipherMode;

    public Cipher(String key, String initVector) {
        this.key = key;
        this.initVector = initVector;
    }

    public void launch(String file) {
        Structure structure = FileProcessor.createStructure(file);
        structure.setData(getNewStructureData(structure, cipherMode));
        System.out.println(structure.getPath() + " " + cipherMode);
        FileProcessor.saveStructure(structure, cipherMode);
        System.out.println(file + " finished.");
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
