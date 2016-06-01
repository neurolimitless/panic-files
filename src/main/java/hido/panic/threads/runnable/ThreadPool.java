package hido.panic.threads.runnable;

import hido.panic.threads.listeners.TaskCompleteEvent;
import hido.panic.threads.listeners.TaskCompleteListener;
import hido.panic.threads.listeners.TaskStartedListener;
import hido.panic.threads.listeners.WorkCompleteListener;

import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by sgnatiuk on 6/2/15.
 */
public final class ThreadPool {

    private static final int CPU_UNITS = Runtime.getRuntime().availableProcessors();

    private TaskCompleteListener taskCompleteListener;
    private TaskCompleteListener externalTaskCompleteListener;
    private TaskStartedListener taskStartedListener;
    private WorkCompleteListener workCompleteListener;

    private List<Runnable> scheduledTasks;

    private final ThreadPoolExecutor executorService;

    private volatile ControllerState controllerState;


    private ThreadPool(){
        scheduledTasks = Collections.synchronizedList(new ArrayList<>());
        controllerState = ControllerState.STOPPED;
        taskCompleteListener = createTaskCompleteListener();
        executorService = buildThreadPoolExecutor();
    }

    private static class ThreadControllerHolder {
        public static final ThreadPool HOLDER_INSTANCE = new ThreadPool();
    }

    public static ThreadPool getInstance(){
        return ThreadControllerHolder.HOLDER_INSTANCE;
    }

    private TaskCompleteListener createTaskCompleteListener(){
        TaskCompleteListener taskCompleteListener = new TaskCompleteListener() {
            @Override
            public void actionPerformed(TaskCompleteEvent event) {
                if(externalTaskCompleteListener != null){
                    externalTaskCompleteListener.actionPerformed(event);
                }

                if(executorService.getQueue().isEmpty()){
                    alertTasksFinished();
                }
            }
        };
        return taskCompleteListener;
    }

    private void alertTasksFinished() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (controllerState) {
                    if (controllerState != ControllerState.STOPPED) {
                        if (isWorkFinished()) {
                            controllerState = ControllerState.STOPPED;
                            notifyWorkFinished();
                        }
                    }
                }
            }
        }).start();
    }

    private void notifyWorkFinished() {
        if (workCompleteListener != null) {
            workCompleteListener.actionPerformed();
        }
    }

    private boolean isWorkFinished() {
        return executorService.getQueue().isEmpty()
                && scheduledTasks.isEmpty()
                && executorService.getActiveCount() == 0;
    }

    public void registerThread(CipherRunnable cipherThread){
        cipherThread.addTaskStartedListener(taskStartedListener);
        cipherThread.addTaskCompleteListener(taskCompleteListener);

        if(controllerState == ControllerState.STARTED){
            executorService.execute(cipherThread);
        }else {
            scheduledTasks.add(cipherThread);
        }
    }

    public void registerThreads(Collection<CipherRunnable> cipherThreads){
        cipherThreads.forEach(this::registerThread);
    }

    public void registerWorkCompleteListener(WorkCompleteListener workCompleteListener){
        this.workCompleteListener = workCompleteListener;
    }


    public void registerTaskCompleteListener(TaskCompleteListener taskCompleteListener){
        this.externalTaskCompleteListener = taskCompleteListener;
    }

    public void registerTaskStartedListener(TaskStartedListener taskStartedListener){
        this.taskStartedListener = taskStartedListener;
    }

    private ThreadPoolExecutor buildThreadPoolExecutor(){
        return new ThreadPoolExecutor(CPU_UNITS, CPU_UNITS, 0L, TimeUnit.MILLISECONDS,
                new PriorityBlockingQueue<>(CPU_UNITS, new Comparator<Runnable>() {
            @Override
            public int compare(Runnable o1, Runnable o2) {
                if(o1 instanceof CipherRunnable
                        && o2 instanceof CipherRunnable){
                    CipherRunnable obj1 = (CipherRunnable) o1;
                    CipherRunnable obj2 = (CipherRunnable) o2;
                    return obj2.getPriority() - obj1.getPriority();
                }
                return 0;
            }
        }));
    }

    public void start(){
        if(controllerState == ControllerState.STARTED){
            throw new UnsupportedOperationException("Controller already started. Current state = "+controllerState);
        }
        synchronized (scheduledTasks) {
            controllerState = ControllerState.STARTED;
            scheduledTasks.forEach(executorService::execute);
            scheduledTasks.clear();
        }
    }

    public void stop(){
        controllerState = ControllerState.STOPPED;
        scheduledTasks.clear();
        executorService.getQueue().clear();
    }

    public void pause(){
        if(controllerState != ControllerState.STARTED){
            throw new UnsupportedOperationException("Controller is not started. Current state = "+controllerState);
        }
        synchronized (scheduledTasks){
            controllerState = ControllerState.PAUSED;
            scheduledTasks.clear();
            scheduledTasks.addAll(executorService.getQueue());
            executorService.getQueue().clear();
        }
    }

    public void resume(){
        if(controllerState != ControllerState.PAUSED){
            throw new UnsupportedOperationException("Controller is not paused. Current state = "+controllerState);
        }
        start();
    }

    public ControllerState getControllerState() {
        return controllerState;
    }

    public void shutdown(){
        executorService.shutdown();
    }

    public enum ControllerState{
        PAUSED, STARTED, STOPPED;
    }


}
