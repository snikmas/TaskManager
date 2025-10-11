package manager;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.HashMap;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    private Map<Long, Task> tasks = new HashMap<Long, Task>();

    Long taskId = 1L;


    @Override
    public void createTask(Task task){

        // finish

        tasks.put(task.getTaskId(), task);


        return;
    };

    @Override
    public void viewAllTasks(){
        if(tasks.isEmpty()){
            System.out.println("No tasks have been added yet.");
        }

        for(Task task : tasks.values()){
            System.out.println(task.toString() + "\n");

            if(task instanceof Subtask){
                System.out.println("   Belongs to: " + tasks.get(((Subtask) task).getTaskId()).getTaskId());
            } else if(task instanceof Epic epic){
                if(epic.getSubtasks().isEmpty()){
                    break;
                }
                System.out.println("Related Subtasks:");
                for(int ii = 0; ii < epic.getSubtasks().size(); ii++){
                    Subtask subtask = epic.getSubtasks().get(ii);
                    System.out.println((ii + 1) + ".Task ID: " + subtask.getTaskId());
                    System.out.println("   Task Name: " + subtask.getTaskName() + "\n");
                }
            }
        }
        return;
    };

    @Override
    public void deleteAllTasks(){
        return;
    };

    @Override
    public Task getTaskById(Long taskId){
        return tasks.get(taskId);

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

     public Long generatorTaskId(){
        return taskId++;
    }

    // later neew to add such kind of functoins to the utils
    public boolean epicAvailable(){
        for(Task task : tasks.values()){
            if(task instanceof Epic){
                return true;
            }
        }
        return false;
    }
}
