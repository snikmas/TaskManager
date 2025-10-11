package tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Epic extends Task{

    private List<Subtask> subtasks = new ArrayList<>();



    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    @Override
    public String getType(Task task) {
        return "Epic";
    }



}
