package Managers;

import Managers.Interfaces.TaskManager;
import Tasks.Epic;
import Tasks.Status;
import Tasks.Subtask;
import Tasks.Task;
import Utils.Utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static Utils.Utils.parseStatus;

public class FileBackendTaskManager extends InMemoryTaskManager implements TaskManager {

    //   ============================
    //          BASICS

    // CREATE AND SAVE TO THE FILE
    @Override
    public void createTask(Task task){
        super.createTask(task);
        save(); // save - save current 状态

    }
    @Override
    public void createTask(Subtask subtask){
        super.createTask(subtask);
        save(); // save - save current

    }
    @Override
    public void createTask(Epic epic){
        super.createTask(epic);
        save(); // save - save current

    }

    //================================
    //            UPDATE AND SAVE
    @Override
    public void updateTask(Task task, Long id){
        super.updateTask(task, id);
        save();
    }

    @Override
    public void deleteAllTasks(){
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteTaskById(Long taskId){
        super.deleteTaskById(taskId);
        save();
    }
    // ============================

    String firstLine = "id,type,title,status,description\n";
    private final Path filePath;

    // ADD MORE
    public FileBackendTaskManager(){

        String pathString = System.getProperty("user.dir");
        this.filePath = Paths.get(pathString, "src", "Data", "data.csv");
        if (!Files.exists(filePath)) {
            try {
                Files.createFile(filePath);
                Files.writeString(filePath, firstLine);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // load current file.. and compare with the current?
        load();
    }
    // fromat:
    public void save(){


    }

    public void load() {

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath.toFile()))) {
            // skip the 1st line
            bufferedReader.readLine();
            String line;
            String[] data;

            while ((line = bufferedReader.readLine()) != null && !line.equals("History:")) {
                data = line.trim().split(",");
                // check for a data
                if (data.length < 5) continue;

                Task task = null;
                Long taskId = Long.parseLong(data[0].trim());
                String type = data[1].trim();
                String title = data[2].trim();
                Status status = Utils.parseStatus(data[3].trim());
                String description = data[4].trim();


                switch (type) {
                    case "Subtask" -> {
                        task = new Subtask();
                        if (data.length > 5) {
                            ((Subtask) task).setParentId(Long.parseLong(data[5].trim()));
                        }
                    }
                    case "Epic" -> {
                        task = new Epic();
                    }
                    default -> task = new Task();
                }

                task.setTaskId(taskId);
                task.setTaskTitle(title);
                task.setStatus(status);
                task.setDescription(description);

                if (getAllTypesTasks().containsKey(taskId)) {
                    // rewrite from the file
                    getAllTypesTasks().put(taskId, task);
                    // update existing
                    if (task instanceof Task) {
                        getTasks().put(taskId, (Task) task);
                    } else if (task instanceof Subtask) {
                        getSubtasks().put(taskId, (Subtask) task);
                    } else if (task instanceof Epic) {
                        getEpics().put(taskId, (Epic) task);
                    }
                } else {
                    // add a new task
                    getAllTypesTasks().put(taskId, task);
                    if (task instanceof Task) {
                        getTasks().put(taskId, (Task) task);
                    } else if (task instanceof Subtask) {
                        getSubtasks().put(taskId, (Subtask) task);
                    } else if (task instanceof Epic) {
                        getEpics().put(taskId, (Epic) task);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке файла: " + e.getMessage());
        }

    }

}
