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

    // 1.
    public List<Task> getAllTasks();
    // 2.
    public void updateTask(Task task, Long taskId);
    // 3.
    public Task getTaskById(Long taskId);
    // getEpic / getTask / getSubtask
    // 4.
    public List<Subtask> getEpicSubtasks(Epic epic);
    // 5.
    public void deleteAllTasks();
    // 6.
    public void deleteTaskById(Long taskId);
    // 7.
    public List<Task> history();
    // 8. save tasks


    // helpers
    public void createTask(Task task);
    public void createTask(Subtask subtask);
    public void createTask(Epic epic);
    // for unit test!
    public void createTask(Task task, String title, String description);


    // get an object that should completely replace the old: updateEpic/Subtask/Task epics.getId(), epic
    // returns last 10
    // create a list for hasBeenChecked tasks ?


    public HashMap<Long, Epic> getEpics();

    public HashMap<Long, Subtask> getSubtasks();

    public HashMap<Long, Task> getTasks();

    public HashMap<Long, Task> getAllTypesTasks();
}
