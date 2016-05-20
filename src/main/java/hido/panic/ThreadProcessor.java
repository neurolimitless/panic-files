package hido.panic;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadProcessor {
    public final int CORE_COUNT;

    public ThreadProcessor() {
        CORE_COUNT = Runtime.getRuntime().availableProcessors(); //retrieving how much cores has processor
    }
    public void execute(List<String> paths,int mode){
        ExecutorService service = Executors.newFixedThreadPool(CORE_COUNT*2);
        System.out.println("Launches with "+CORE_COUNT*2+" threads.");
        for (String path : paths) {
            System.out.println(path+" added to queue.");
            service.execute(new CipherThread(path,Cipher.AES_CFB,mode));
        }
        service.shutdown();
    }
}
