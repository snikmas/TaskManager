package Managers.Interfaces;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

import java.util.HashMap;
import java.util.List;
//
//". Create a new task",
//        ". Update a task",
//        ". Get a task by id",
//        ". Get All Tasks",
//        ". Get Epic's subtasks",
//        ". Delete All Tasks",
//        ". Delete a task by id",
//        ". Get History",
//        ". Save tasks",
public interface TaskManager {

    public List<Task> getAllTasks();
    public void deleteAllTasks();
    public Task getTaskById(Long taskId);
    // getEpic / getTask / getSubtask
    public void deleteTaskById(Long taskId);

    // crete 3 hashmaps for tasks/subtasks/epic
    public void createTask(Task task);
    public void createTask(Subtask subtask);
    public void createTask(Epic epic);

    // get an object that should completely replace the old: updateEpic/Subtask/Task epics.getId(), epic
    public void updateTask(Task task, Long taskId);
    public List<Subtask> getEpicSubtasks();

    // returns last 10
    // create a list for hasBeenChecked tasks
    public List<Task> history();


    public HashMap<Long, Epic> getEpics();

    public HashMap<Long, Subtask> getSubtasks();

    public HashMap<Long, Task> getTasks();

}
