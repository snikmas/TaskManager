package manager;

import tasks.Task;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Queue;
// task: no duplicates in the history

public class InMemoryHistoryManager implements HistoryManager {

    static class ListNode{
        Task task;
        ListNode next;
        ListNode prev;
        int index;

        ListNode(){};
        ListNode(Task task){
            this.task = task;
            this.index = counterNodes++;
        }
    }


    // util
    static int counterNodes = 0;
    private int size = 0;
    private final int MAX_HISTORY_SIZE = 10;

    public ListNode historyNodeStart = null;
    public ListNode historyNodeEnd = null;

    public final HashMap<Long, Integer> historyMap = new HashMap<Long, Integer>();

    // for injection
    public InMemoryHistoryManager() {}

    @Override
    public void add(Task task){

        while (size >= MAX_HISTORY_SIZE) {
            if (historyNodeStart != null) {
                historyNodeStart = historyNodeStart.next;
                if (historyNodeStart != null) {
                    historyNodeStart.prev = null;
                }
            }
            size--;
        }

        ListNode newNode = new ListNode(task);
        historyMap.put(task.getTaskId(), newNode.index);


        if(historyNodeStart == null){
            historyNodeStart = newNode;
            historyNodeEnd = newNode;

        } else {
            historyNodeEnd.next = newNode;
            newNode.prev = historyNodeEnd;
            historyNodeEnd = newNode;
        }
        size++;

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

    @Override
    public void remove(long taskId) {
        ListNode removeNode = historyNodeStart;

        while (removeNode != null && removeNode.index != taskId) {
            removeNode = removeNode.next;
        }

        if (removeNode == null) return; // not found

        if (removeNode.prev != null) {
            removeNode.prev.next = removeNode.next;
        } else {
            historyNodeStart = removeNode.next; // removing head
        }

        if (removeNode.next != null) {
            removeNode.next.prev = removeNode.prev;
        } else {
            historyNodeEnd = removeNode.prev; // removing tail
        }

        System.out.println("Deleted Successfully");
        // No need to set removeNode = null
    }

}
