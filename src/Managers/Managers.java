package Managers;

import Managers.Interfaces.HistoryManager;
import Managers.Interfaces.TaskManager;

// here we will create all managers
public class Managers {

    static InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    static InMemoryTaskManager taskManager = new InMemoryTaskManager();
    static FileBackendTaskManager fileBackendTaskManager = new FileBackendTaskManager();
    static BackendFileTaskManager backendFileTaskManager = new BackendFileTaskManager();
    static HttpTaskServer taskServer = new HttpTaskServer();

    // get managers from here
    public static TaskManager getDefault(){
        return taskManager;
    }
    public static HttpTaskServer getTaskServer(){ return taskServer;}
    public static BackendFileTaskManager getBackendFileTaskManager() { return backendFileTaskManager; }
    public static HistoryManager getDefaultHistory(){
        return historyManager;
    }
    public static FileBackendTaskManager getDefaultFileBackendManager() { return fileBackendTaskManager;};

}

