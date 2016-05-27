package hido.panic.cipher;

public enum CipherType {
    AES_CFB;

    public static CipherType parseCipherType(String cipherTypeParam) {
        try {
            if (cipherTypeParam != null && !cipherTypeParam.isEmpty()) {
                return CipherType.valueOf(cipherTypeParam);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Wrong cipher algorithm.");
        }
        return null;
    }
}
