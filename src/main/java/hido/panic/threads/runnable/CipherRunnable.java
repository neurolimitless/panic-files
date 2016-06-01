package hido.panic.threads.runnable;

import hido.panic.threads.listeners.TaskCompleteEvent;
import hido.panic.threads.listeners.TaskCompleteListener;
import hido.panic.threads.listeners.TaskStartedEvent;
import hido.panic.threads.listeners.TaskStartedListener;

/**
 * Created by sgnatiuk on 6/2/15.
 */
public abstract class CipherRunnable implements Runnable {

    public static final int LOW_PRIORITY = 0;
    public static final int NORMAL_PRIORITY = 1;
    public static final int HIGH_PRIORITY = 2;

    private int priority;

    private static int ID_SOURCE = 0;

    private final int id;

    private TaskStartedListener taskStartedListener;
    private TaskCompleteListener taskCompleteListener;

    public CipherRunnable() {
        this(NORMAL_PRIORITY);
    }

    public CipherRunnable(int priority) {
        this.priority = priority;
        id = ID_SOURCE++;
    }

    @Override
    public final void run() {
        alertTaskStarted();
        try {
            doWork();
        } finally {
            alertTaskCompleted();
        }
    }

    private void alertTaskStarted(){
        if (taskStartedListener != null) {
            taskStartedListener.actionPerformed(createTaskStartedEvent());
        }
    }

    private void alertTaskCompleted(){
        if (taskCompleteListener != null) {
            taskCompleteListener.actionPerformed(createTaskCompleteEvent());
        }
    }

    protected abstract void doWork();

    protected abstract TaskStartedEvent createTaskStartedEvent();

    protected abstract TaskCompleteEvent createTaskCompleteEvent();

    public void addTaskCompleteListener(TaskCompleteListener taskCompleteListener) {
        this.taskCompleteListener = taskCompleteListener;
    }

    public void addTaskStartedListener(TaskStartedListener taskStartedListener) {
        this.taskStartedListener = taskStartedListener;
    }

    public int getId() {
        return id;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return "SearchRunnable{" +
                "id=" + id +
                '}';
    }
}