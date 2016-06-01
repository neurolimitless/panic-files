package hido.panic.threads.listeners;

import java.util.List;

/**
 * Created by sgnatiuk on 6/5/15.
 */
public class TaskStartedEvent {

    private int taskId;
    private List<String> filesToProcess;

    public TaskStartedEvent(int taskId, List<String> filesToProcess) {
        this.taskId = taskId;
        this.filesToProcess = filesToProcess;
    }

    public int getTaskId() {
        return taskId;
    }

    public List<String> getFilesToProcess() {
        return filesToProcess;
    }
}
