package manager;

public class Managers {

    static HistoryManager historyManager;

    static HistoryManager getDefaultHistory() {
        if (historyManager == null) {
            return null;
        }
        return historyManager;
    }

    TaskManager getDefault() {
//        TaskManager manager = new TaskManager();
        return null;
    }
}
