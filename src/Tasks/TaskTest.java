package Tasks;

import Managers.Interfaces.TaskManager;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static Managers.Managers.getDefault;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    TaskManager taskManager = getDefault();


    // OK, NO PROBLEMS
    @Test
    void addNewTask(){
        Task task = new Task();
        String title = "title-1";
        String description = "des-1";
        taskManager.createTask(task, title, description);
        final Long taskId = task.getTaskId();

        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "The task hasn't been found");
        assertEquals(task, savedTask, "The tasks are not the same.");

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Tasks don't return");
        assertEquals(1, tasks.size(), "Wrong tasks amount");
        assertEquals(task, tasks.get(0), "The tasks doesn't the same");



    }

}