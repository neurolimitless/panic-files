import hido.panic.cipher.Cipher;
import hido.panic.cipher.CipherFactory;
import hido.panic.cipher.CipherMode;
import hido.panic.cipher.CipherType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertTrue;

public class EncryptionTest {

    File testFile;
    String filename = "testfile.dat";

    @Before
    public void createTestFile() throws IOException {
        testFile = new File(filename);
        String data = "Simple string to encrypt";
        FileWriter fileWriter = new FileWriter(testFile);
        fileWriter.write(data);
        fileWriter.close();
    }

    @Test
    public void testEncryption(){
        String key = "Key";
        String initVector = "InitVector";
        Cipher cipher = CipherFactory.factory(CipherType.AES_CFB, key, initVector);
        cipher.setCipherMode(CipherMode.ENCRYPTION);
        cipher.launch(filename);
    }

    @After
    public void checkResults() throws IOException{
        String result = "91295adf365801075d430f71851da1da498d9b8c47cde17e";
        BufferedReader bufferedReader = new BufferedReader(new FileReader(testFile));
        String encryptionResult = bufferedReader.readLine();
        assertTrue(result.equals(encryptionResult));
        bufferedReader.close();
        testFile.delete();
    }
}