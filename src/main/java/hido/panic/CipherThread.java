package hido.panic;

public class CipherThread implements Runnable {

    FileProcessor fileProcessor = new FileProcessor();
    CipherProcessor cipherProcessor = new CipherProcessor();
    private final String file;
    private final Cipher cipher;
    private final int mode;
    //TODO fix this hardcoded shit
    private String key = "somewordsomeword";
    private String initVector = "somepasssomeword";

    public CipherThread(String file, Cipher cipher, int mode) {
        this.file = file;
        this.cipher = cipher;
        this.mode = mode;
    }

    @Override
    public void run() {
        Structure structure = fileProcessor.createStructure(file);
        if (cipher == Cipher.AES_CFB) {
            structure.setData(cipherProcessor.AES_CFB(key, initVector, structure.getData(), mode));
        }
        fileProcessor.saveStructure(structure, mode);
        System.out.println(file + " finished.");
    }
}
