package manager;

import tasks.Task;

import java.util.List;
import java.util.Map;

public interface TaskManager {

    public boolean checkParentId(Long parentId);

    public void viewAllTasks();
    public Task getTaskById(Long taskId);
    public void deleteAllTasks();
    public void createTask(Task task);


    public void updateTask(Task task);
    public void deleteById(Long taskId);
    public void viewTask(Task task);


    public void getEpicSubtasks(Long epicId);



}
