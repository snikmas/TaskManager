package Managers;
import Managers.Interfaces.TaskManagerUtils;
import Tasks.Epic;
import Tasks.Status;
import Utils.Utils;
import Managers.Interfaces.TaskManager;
import Tasks.Subtask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static Utils.Utils.*;

// delete abstract
public class InMemoryTaskManager implements TaskManager {

    private HashMap<Long, Task> tasks = new HashMap<>();
    private HashMap<Long, Subtask> subtasks = new HashMap<>();
    private HashMap<Long, Epic> epics = new HashMap<>();
    static Long id = 1L;


    // MAIN FINCTIONS
    // create 3 hashmaps for tasks/subtasks/epic
    public void createTask(Task task) {
        // just fill info, later can input to the func
        System.out.println("Title:");
        task.setTaskTitle(Utils.getInput());
        System.out.println("Description:");
        task.setDescription(Utils.getInput());

        task.setStatus(Status.NEW);
        task.setTaskId(id);

        tasks.put(id, task);
        id++;
    }

    public void createTask(Subtask subtask) {

        // just fill info
        System.out.println("Title:");
        subtask.setTaskTitle(Utils.getInput());
        System.out.println("Description:");
        subtask.setDescription(Utils.getInput());

        System.out.println("Epic Id:");
        while(true){
            long epicId = Utils.getLongInput();
            if(!epics.containsKey(epicId)){
                System.out.println("Wrong ID! Try again!");
            } else {
                // also has to check epic's status
                Utils.changeEpicStatus(epics.get(epicId));
                subtask.setParentId(epicId);
                break;
            }
        }

        subtask.setStatus(Status.NEW);
        subtask.setTaskId(id);

        subtasks.put(id, subtask);
        id++;
    }

    public void createTask(Epic epic) {

        // just fill info
        System.out.println("Title:");
        epic.setTaskTitle(Utils.getInput());
        System.out.println("Description:");
        epic.setDescription(Utils.getInput());
        epic.setStatus(Status.NEW);
        epic.setTaskId(id);

        epics.put(id, epic);
        id++;

    }

    public List<Task> getAllTasks() {
        return null;
    }

    public void deleteAllTasks() {

    }

    public Task getTaskById(Long taskId) {
        return null;
    }

    // getEpic / getTask / getSubtask
    public void deleteTaskById(Long taskId) {

    }


    // get an object that should completely replace the old: updateEpic/Subtask/Task epics.getId(), epic
    public void updateTask(Task task, Long taskId) {

    }

    public List<Subtask> getEpicSubtasks() {
        return null;
    }

    // returns last 10
    // create a list for hasBeenChecked tasks
    public List<Task> history() {
        return null;
    }


    public HashMap<Long, Epic> getEpics() {
        return epics;
    }

    public HashMap<Long, Subtask> getSubtasks() {
        return subtasks;
    }

    public HashMap<Long, Task> getTasks() {
        return tasks;
    }

}