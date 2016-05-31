import hido.panic.cipher.Cipher;
import hido.panic.cipher.CipherFactory;
import hido.panic.cipher.CipherMode;
import hido.panic.cipher.CipherType;
import hido.panic.file.FileUtilsIO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class EncryptionTest {

    private String fileName = "testfile.dat";
    private File testFile = new File(fileName);
    private List<String> fileDataToEncrypt = Arrays.asList(
            "Simple string to encrypt 1",
            "Simple string to encrypt 2"
    );

    private String key = "Key";
    private String initVector = "InitVector";

    private Cipher cipher;

    @Before
    public void createTestFile() throws IOException {

        cipher = CipherFactory.factory(CipherType.AES_CFB, key, initVector);

        //create file to test
        FileUtilsIO.writeToFile(testFile, fileDataToEncrypt, true);
    }

    @Test
    public void testEncryption() throws IOException {
        //encrypt file and read encrypted file
        encryptFile(fileName);
        List<String> dataFromEncryptedFile = FileUtilsIO.readFile(testFile);

        //verify that data is encrypted
        assertNotEquals(fileDataToEncrypt, dataFromEncryptedFile);
        System.out.println(fileDataToEncrypt);
        System.out.println(dataFromEncryptedFile);
    }

    @Test
    public void testDecryption() throws IOException {
        //restore file and read decrypted data
        decryptFile(fileName);
        List<String> dataFromDecryptedFile = FileUtilsIO.readFile(testFile);

        //verify that data is restored successfully
        assertEquals(fileDataToEncrypt, dataFromDecryptedFile);
        System.out.println(fileDataToEncrypt);
        System.out.println(dataFromDecryptedFile);
    }

    @After
    public void checkResults() throws IOException{
        //delete test file
//        testFile.delete();
    }

    private void encryptFile(String filename){
        cipher.setCipherMode(CipherMode.ENCRYPTION);
        cipher.launch(filename);
    }

    private void decryptFile(String filename){
        cipher.setCipherMode(CipherMode.DECRYPTION);
        cipher.launch(filename);
    }
}