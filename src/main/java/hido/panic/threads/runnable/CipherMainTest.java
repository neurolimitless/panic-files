package hido.panic.threads.runnable;

import hido.panic.cipher.Cipher;
import hido.panic.cipher.CipherFactory;
import hido.panic.cipher.CipherType;
import hido.panic.threads.listeners.WorkCompleteListener;

import java.util.Arrays;

/**
 * Created by Sergiy on 7/4/2015.
 */
public class CipherMainTest {

    public static void main(String[] args) {
        String path = "./src";
        Cipher cipher = CipherFactory.factory(CipherType.AES_CFB, "key","vector");
        ThreadPool.getInstance().registerThread(new HierarchyWalker(Arrays.asList(path), cipher));
        ThreadPool.getInstance().registerWorkCompleteListener(new WorkCompleteListener() {
            @Override
            public void actionPerformed() {
                System.out.println("DONE!!!");
                ThreadPool.getInstance().shutdown();
            }
        });
        ThreadPool.getInstance().start();
    }
}


/*


 */