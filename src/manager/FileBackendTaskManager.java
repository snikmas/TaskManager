package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileBackendTaskManager extends InMemoryTaskManager {

    public FileBackendTaskManager(HistoryManager historyManager) {
        super(historyManager);
    }

    @Override
    public void createTask(Task task){
        super.createTask(task);
        save(task);
    }

    public void save(Task task){

        if(task == null){
            return;
        }
        // csv file:
        // 1st line: id/number,type,name,status,description,epic
        // next all tasks . . .
        // next emppty line
        // history

        // hints: overwrite toString(Task taks) and write for a csv
        // + function to read a task from the line: List<INteger> fromString -> for backup

        // input data:
        // 1. create file is there're no (need only one file? for now)
        // 2.1. add the 1st line: format
        // 2.2. add all tasks, using toStrnig
        // 3. add space and do the same for the history
        // 4. save and close
        String firstLine = "id,type,name,status,description,epic\n";

        String path = System.getProperty("user.dir");
        Path thisFilePath = Paths.get(path, "src", "data", "tasks.csv");

        try {
            // Ensure the parent directory exists first
            if (Files.notExists(thisFilePath.getParent())) {
                Files.createDirectories(thisFilePath.getParent());
            }

            if (Files.notExists(thisFilePath)) {
                Files.createFile(thisFilePath);
                System.out.println("Created file: " + thisFilePath);
            } else {
                System.out.println("File already exists: " + thisFilePath);
            }

        } catch (IOException e) {
            System.out.println("Oops, something went wrong with files: " + e.getMessage());
        }

        boolean isEmpty = false;
        // --- Reading ---
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(thisFilePath.toFile()))) {
            String line;
            if ((line = bufferedReader.readLine()) == null) {
                isEmpty = true;
            }
        } catch (IOException e) {
            System.out.println("File not found or could not read: " + thisFilePath);
        }

        // --- Writing ---
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(thisFilePath.toFile(), true))) {
            if(isEmpty){
                bufferedWriter.write(firstLine);
            }
            String taskInfo = toString(task);
            bufferedWriter.write(taskInfo);

        } catch (IOException e) {
            System.out.println("Could not write to file: " + thisFilePath);
        }



        System.out.println(thisFilePath);
    }


    public String toString(Task task){
        String firstLine = "id,type,name,status,description,epic\n";
        if(task.getTaskType().equals("epic")){
            StringBuilder lastParameter = new StringBuilder("subtasks:");
            for(Subtask subtask : ((Epic) task).getSubtasks()){
                lastParameter.append(subtask.getTaskId()).append(";");
            }
            return String.format("%d,%s,%s,%s,%s,%s", task.getTaskId(), task.getTaskName(), task.getTaskType(), task.getTaskStatus(), task.getTaskDesc(), lastParameter);

        } else if (task.getTaskType().equals("subtask")) {
            return String.format("%d,%s,%s,%s,%s,parentId:%d", task.getTaskId(), task.getTaskName(), task.getTaskType(), task.getTaskStatus(), task.getTaskDesc(), (((Subtask)task).getParentId()));
        }
        return String.format("%d,%s,%s,%s,%s,task", task.getTaskId(), task.getTaskName(), task.getTaskType(), task.getTaskStatus(), task.getTaskDesc());
    }
}
