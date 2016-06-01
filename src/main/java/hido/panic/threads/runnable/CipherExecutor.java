package hido.panic.threads.runnable;

import hido.panic.cipher.Cipher;
import hido.panic.threads.listeners.TaskCompleteEvent;
import hido.panic.threads.listeners.TaskStartedEvent;

import java.io.File;
import java.util.Arrays;

/**
 * Created by sgnatiuk on 6/15/15.
 */
public class CipherExecutor extends CipherRunnable {

    private Cipher cipher;
    private File file;

    public CipherExecutor(Cipher cipher, File file, int priority) {
        super(priority);
        this.cipher = cipher;
        this.file = file;
    }

    public CipherExecutor(Cipher cipher, File file) {
        this(cipher, file, HIGH_PRIORITY);
    }

    @Override
    protected void doWork() {
        cipher.launch(file);
        System.out.println(file.getAbsolutePath());
    }

    @Override
    protected TaskStartedEvent createTaskStartedEvent() {
        return new TaskStartedEvent(getId(), Arrays.asList(file.getAbsolutePath()));
    }

    @Override
    protected TaskCompleteEvent createTaskCompleteEvent() {
        return new TaskCompleteEvent(Arrays.asList(file.getAbsolutePath()));
    }

    @Override
    public String toString() {
        return "CipherExecutor{" +
                "file=" + file +
                '}';
    }
}
