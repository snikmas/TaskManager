package Managers.Interfaces;

import Tasks.Task;

import java.util.List;

// FOR MANAGE HISTORY. NOT REALEZATION
public interface HistoryManager {
    // only 2 methods
    void add(Task task);
    void remove(Long id);
    List<Task> getHistory();

}
