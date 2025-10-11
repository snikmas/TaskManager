    package tasks;

    public class Task  {

        private Long taskId;
        private String taskName;
        private String taskDesc;
        private Status taskStatus;

        private String taskType;

        public Task(Long taskId, String taskName, String taskDesc, Status taskStatus) {
            this.taskId = taskId;
            this.taskName = taskName;
            this.taskDesc = taskDesc;
            this.taskStatus = taskStatus;
            this.taskType = taskStatus.toString();
        }

        public Task() {}

        @Override
        public String toString() {
            return "TaskId: " + taskId + "\nTaskType: " + taskType + "\nTaskName: " + taskName + "\nTaskDesc: " + taskDesc + "\nTaskStatus: " + taskStatus;
        }

        public Long getTaskId() {
            return taskId;
        }

        public void setTaskId(Long taskId) {
            this.taskId = taskId;
        }

        public String getTaskName() {
            return taskName;
        }

        public void setTaskName(String taskName) {
            this.taskName = taskName;
        }

        public String getTaskDesc() {
            return taskDesc;
        }

        public void setTaskDesc(String taskDesc) {
            this.taskDesc = taskDesc;
        }


        public String getType(Task task) {
            return taskType;
        }

        public Status getTaskStatus() {
            return taskStatus;
        }

        public void setTaskStatus(Status taskStatus) {
            this.taskStatus = taskStatus;
        }




    }
