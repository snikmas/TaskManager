package Managers;

import Managers.Interfaces.HistoryManager;
import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private final int SIZE = 10;           // Максимальный размер истории


    private Map<Long, Node> historyMap = new HashMap<>();
    private Node head;
    private Node tail;
    private int size = 0;

    @Override
    public void add(Task task) {
        if (historyMap.containsKey(task.getTaskId())) {
            removeNode(historyMap.get(task.getTaskId()));
        }

        Node newNode = new Node(task);
        addNodeAtHead(newNode);
        historyMap.put(task.getTaskId(), newNode);
        size++;

        if (size > SIZE) {
            historyMap.remove(tail.task.getTaskId());
            removeNode(tail);
            size--;
        }
    }

    @Override
    public void delete(Long id) {
        Node node = historyMap.get(id);
        if (node != null) {
            removeNode(node);
            historyMap.remove(id);
            size--;
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> tasks = new ArrayList<>();
        Node current = head;
        while (current != null) {
            tasks.add(current.task);
            current = current.next;
        }
        return tasks;
    }

    @Override
    public void deleteAllTasks() {
        head = null;
        tail = null;
        historyMap.clear();
        size = 0;
    }

    private void addNodeAtHead(Node node) {
        node.next = head;
        node.prev = null;
        if (head != null) head.prev = node;
        head = node;
        if (tail == null) tail = head;  // Если список был пуст
    }

    private void removeNode(Node node) {
        if (node.prev != null) node.prev.next = node.next;
        else head = node.next; // удаляем голову

        if (node.next != null) node.next.prev = node.prev;
        else tail = node.prev; // удаляем хвост
    }

    private static class Node {
        Task task;
        Node prev;
        Node next;

        Node(Task task) {
            this.task = task;
        }
    }


    public Map<Long, Node> getHistoryMap() {
        return historyMap;
    }

    public Node getHead() {
        return head;
    }
}
