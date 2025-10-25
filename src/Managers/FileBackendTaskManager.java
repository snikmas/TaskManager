package Managers;

import Managers.Interfaces.TaskManager;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileBackendTaskManager extends InMemoryTaskManager implements TaskManager {

    //   ============================
    //          BASICS

    // CREATE AND SAVE TO THE FILE
    @Override
    public void createTask(Task task){
        super.createTask(task);
        save(); // save - save current

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

    String firstLine = "id,typemname,status,description\n";
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

        // load data
        load();
    }
    // fromat:
    // id,type,name,status,description
    public void save(){

    }x

}
