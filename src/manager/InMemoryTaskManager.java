package manager;

import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.HashMap;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    private Map<Long, Task> tasks = new HashMap<Long, Task>();

    Long taskId = 1L;

    @Override
    public void viewAllTasks(){
        System.out.println("getAllTasks");
        return;
    };

    @Override
    public void deleteAllTasks(){
        return;
    };

    @Override
    public Task getTaskById(Long taskId){
        return null;
    };

    @Override
    public void createTask(Task task){

        // finish: add id

        task.setTaskId(taskId);
        tasks.put(taskId, task);

        generatorTaskId();

        return;
    };

    @Override
    public void updateTask(Task task){
        return;
    };

    @Override
    public void deleteById(Long taskId){
        return;
    };

    @Override
    public void getEpicSubtasks(Long epicId){
        return;
    };

    public Map<Long, Task> getTasks() {
        return tasks;
    }

    @Override
    public boolean checkParentId(Long parentId){
        return tasks.containsKey(parentId);
    }


    public void setTasks(Map<Long, Task> tasks) {
        this.tasks = tasks;
    }

     private Long generatorTaskId(){
        return taskId++;
    }
}
