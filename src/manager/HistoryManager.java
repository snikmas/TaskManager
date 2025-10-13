package manager;

import tasks.Task;

import java.util.ArrayDeque;
import java.util.Queue;

public interface HistoryManager {

    public Queue<Task> historyList = new ArrayDeque<>();

    // property tasks: viewed
    public void add(Task task);
    public void getHistory();

}
