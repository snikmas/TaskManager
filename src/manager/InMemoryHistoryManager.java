package manager;

import tasks.Task;

import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {

    // Doubly linked list node
    private static class ListNode {
        Task task;
        ListNode prev;
        ListNode next;

        ListNode(Task task) {
            this.task = task;
        }
    }

    private ListNode head;
    private ListNode tail;
    private final HashMap<Long, ListNode> nodeMap = new HashMap<>();
    private final int MAX_HISTORY_SIZE = 10;
    private int size = 0;

    public InMemoryHistoryManager() {}

    @Override
    public void add(Task task) {
        if (nodeMap.containsKey(task.getTaskId())) {
            remove(task.getTaskId());
        }

        if (size >= MAX_HISTORY_SIZE) {
            removeHead();
        }

        ListNode newNode = new ListNode(task);
        if (head == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }

        nodeMap.put(task.getTaskId(), newNode);
        size++;
    }

    @Override
    public void remove(long taskId) {
        ListNode node = nodeMap.remove(taskId);
        if (node == null) return;

        if (node.prev != null) node.prev.next = node.next;
        else head = node.next;

        if (node.next != null) node.next.prev = node.prev;
        else tail = node.prev;

        size--;
    }

    // Removes the oldest task (head)
    private void removeHead() {
        if (head == null) return;

        nodeMap.remove(head.task.getTaskId());
        head = head.next;
        if (head != null) head.prev = null;
        else tail = null;

        size--;
    }

    @Override
    public void getHistory() {
        if (head == null) {
            System.out.println("There's no history yet!");
            return;
        }

        System.out.println("History:");
        ListNode current = head;
        int i = 1;
        while (current != null) {
            Task task = current.task;
            System.out.println(i + ". " + task.getTaskName() + " (" + task.getTaskType() + ")");
            current = current.next;
            i++;
        }
    }
}

