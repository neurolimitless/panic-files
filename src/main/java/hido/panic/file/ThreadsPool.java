package hido.panic.file;

import hido.panic.cipher.Cipher;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadsPool {

    private ExecutorService executorService;

    public ThreadsPool() {
        this(2 * Runtime.getRuntime().availableProcessors());
    }

    public ThreadsPool(int maxThreadsCount) {
        executorService = Executors.newFixedThreadPool(maxThreadsCount);
    }

    public void execute(List<String> paths, Cipher cipher) {
        for (String path : paths) {
            executorService.execute(() -> cipher.launch(path));
        }
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
