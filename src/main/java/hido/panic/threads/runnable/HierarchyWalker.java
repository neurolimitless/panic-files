package hido.panic.threads.runnable;

import hido.panic.cipher.Cipher;
import hido.panic.threads.listeners.TaskCompleteEvent;
import hido.panic.threads.listeners.TaskStartedEvent;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergiy on 6/3/2015.
 */
public class HierarchyWalker extends CipherRunnable {

    private List<String> filePaths;
    private Cipher cipher;

    public HierarchyWalker(List<String> filePaths, Cipher cipher) {
        this(filePaths, cipher, NORMAL_PRIORITY);
    }

    public HierarchyWalker(List<String> filePaths, Cipher cipher, int priority) {
        super(priority);
        this.cipher = cipher;
        this.filePaths = filePaths;
    }

    protected void doWork() {
        for (String filePath : filePaths) {
            processFile(new File(filePath));
        }
    }

    @Override
    protected TaskStartedEvent createTaskStartedEvent() {
        List<String> fileNames = new ArrayList<>();
        for (String filePath : filePaths) {
            fileNames.add(new File(filePath).getAbsolutePath());
        }
        return new TaskStartedEvent(getId(), fileNames);
    }

    @Override
    protected TaskCompleteEvent createTaskCompleteEvent() {
        return new TaskCompleteEvent(filePaths);
    }

    protected void processFile(File file){
        if (file.isDirectory()) {
            invokeNewHierarchyThread(file);
        } else {
            invokeNewCipherThread(file);
        }
    }

    private List<String> buildFullPathForChildren(String parentAbsolutePath, String[] children){
        List<String> fullPathChildren = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        for (int j = 0; j < children.length; j++) {

            stringBuilder.append(parentAbsolutePath)
                    .append(File.separator).append(children[j]);

            fullPathChildren.add(stringBuilder.toString());
            stringBuilder.setLength(0);
        }
        return fullPathChildren;
    }

    protected void invokeNewHierarchyThread(File file){
        if(Files.isSymbolicLink(file.toPath())){
            return;
        }
        String[] files = file.list();
        if (files == null) {
            return;
        }

        invokeNewHierarchyThread(buildFullPathForChildren(file.getAbsolutePath(), files));
    }

    protected void invokeNewHierarchyThread(List<String> filePaths){
        ThreadPool.getInstance().registerThread(new HierarchyWalker(filePaths, cipher,
                LOW_PRIORITY));
    }

    protected void invokeNewCipherThread(File file) {
        ThreadPool.getInstance().registerThread(new CipherExecutor(cipher, file, HIGH_PRIORITY));
    }

    @Override
    public String toString() {
        return "HierarchyWalker{" +
                "filePaths=" + filePaths +
                ", cipher=" + cipher +
                '}';
    }
}
