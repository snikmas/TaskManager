package Tasks;

import Managers.Managers;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    Task epic = new Epic();

    @Test
    void addNewTask(){
        Managers.getDefault().createTask(epic, "Test AddNewTask", "Test addNewTaskDesc");
        final Long taskId = epic.getTaskId();

        assertNotNull(taskId, "the task is not found");
        assertEquals(epic, Managers.getDefault().getTaskById(taskId), "the tasks are not the same");

        final List<Task> tasks = Managers.getDefault().getAllTasks();
        assertNotNull(tasks, "no tasks from the getAllTasks");
        assertEquals(1, tasks.size(), "the list's size is wrong");
        assertEquals(epic, tasks.get(0), "the tasks are not the same");
    }

    @Test
    void addToHistory(){
        Managers.getDefaultHistory().add(epic);
        final List<Task> history = Managers.getDefaultHistory().getHistory();
        assertNotNull(history, "the history is not null");
        assertEquals(1, history.size(), "the history is not empty");
    }

}