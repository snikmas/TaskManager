package tasks;

public class Subtask extends Task {

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    private Long parentId;

    // subtasks can't be viewed separately
//    @Override
//    public String getType(Task task) {
//        return "Subtask";
//    }


}
