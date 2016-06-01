package hido.panic.threads.listeners;

import java.util.List;

/**
 * Created by Sergiy on 6/3/2015.
 */
public class TaskCompleteEvent {

    private List<String> processedFiles;

    public TaskCompleteEvent(List<String> processedFiles) {
        this.processedFiles = processedFiles;
    }

    public List<String> getProcessedFiles() {
        return processedFiles;
    }

}
