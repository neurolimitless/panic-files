package hido.panic;


public enum CipherMode {

    ENCRYPTION(1), DECRYPTION(2);

    private int value;

    CipherMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static CipherMode getModeByValue(int value) {
        for (CipherMode cipherMode : values()) {
            if (cipherMode.value == value) {
                return cipherMode;
            }
        }
        throw new IllegalArgumentException(String.format("%s is not correspond any mode", value));
    }
}
