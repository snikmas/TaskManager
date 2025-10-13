package manager;

import tasks.Task;

import java.util.ArrayDeque;
import java.util.Queue;

public class InMemoryHistoryManager implements HistoryManager {
    // util
    public Queue<Task> historyList = new ArrayDeque<>();
    private final int MAX_HISTORY_SIZE = 10;

    // for injection
    public InMemoryHistoryManager() {
    }

    @Override
    public void add(Task task){
        while(historyList.size() >= MAX_HISTORY_SIZE){
            historyList.poll();
        }
        historyList.add(task);
    }

    @Override
    public void getHistory(){
        if(historyList.isEmpty()){
            System.out.println("There's no history yet!");
            return;
        }

        System.out.println("History:");

        int i = 1;
        for(Task task : historyList){
            System.out.println(i + ". " + task.getTaskName() + " (" + task.getTaskType() + ")\n");
        }
    }
}
