package Managers;

import Managers.Interfaces.HistoryManager;
import Tasks.Task;

import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    // getaHistroy should tasks from linked list to handle as array list
    // realise your own linked list
    // linked list here write
//    add removeNode method
    // hashmao


    @Override
    public void add(Task task) {
        return;
    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public List<Task> getHistory() {
        return List.of();
    }
}

