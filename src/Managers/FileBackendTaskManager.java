package Managers;

import Managers.Interfaces.HistoryManager;
import Managers.Interfaces.TaskManager;
import Tasks.Epic;
import Tasks.Status;
import Tasks.Subtask;
import Tasks.Task;
import Utils.Utils;
import org.w3c.dom.Node;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static Managers.Managers.getDefaultHistory;
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

    String firstLine = "id,type,title,status,description,epic\n";
    private final Path filePath;
    private final Path fileBufferPath;

    // ADD MORE
    public FileBackendTaskManager(){

        String pathString = System.getProperty("user.dir");
        this.filePath = Paths.get(pathString, "src", "Data", "data.csv");
        this.fileBufferPath = Paths.get(pathString, "src", "Data", "buffer.csv");

        if (!Files.exists(filePath)) {
            try {
                Files.createFile(filePath);
                Files.writeString(filePath, firstLine);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (!Files.exists(fileBufferPath)) {
            try {
                Files.createFile(fileBufferPath);
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

    public void load(){

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath.toFile()));
            BufferedWriter bufferedWriterBuffer = new BufferedWriter(new FileWriter(fileBufferPath.toFile()))) {
            // skip the 1st line
            String line = bufferedReader.readLine();
            bufferedWriterBuffer.write(line);
            bufferedWriterBuffer.newLine();

            String[] data;

            // better check history in the next
            while ((line = bufferedReader.readLine()) != null && !line.equals("History:")) {
                Task task = fromCsvToTaskFormat(line);

                if (getAllTypesTasks().containsKey(task.getTaskId())) {
                    Task taskToRewrite = getAllTypesTasks().get(task.getTaskId());
                    bufferedWriterBuffer.write(toCsvFileFormat(taskToRewrite));
                    bufferedWriterBuffer.newLine();

                } else {
                    // add a new task
                    getAllTypesTasks().put(task.getTaskId(), task);
                    if (task instanceof Task) getTasks().put(task.getTaskId(), (Task) task);
                    else if (task instanceof Subtask) getSubtasks().put(task.getTaskId(), (Subtask) task);
                    else if (task instanceof Epic) getEpics().put(task.getTaskId(), (Epic) task);

                    // no problems
                    // write buffer
                    bufferedWriterBuffer.write(line);
                    bufferedWriterBuffer.newLine();
                }
            }


            // here we got history
            if(line != null && line.equals("History:")){
                bufferedWriterBuffer.write(line);
                bufferedWriterBuffer.newLine();
                HistoryManager historyManager = getDefaultHistory();
                List<Task> currentHistory = historyManager.getHistory();
                int count = 0;

                while((line = bufferedReader.readLine()) != null) {

                    Task fileTask = fromCsvToTaskFormat(line);
                    if (fileTask != null && count < currentHistory.size() - 1) {
                        Task currentTask = currentHistory.get(count);

                        if (currentTask != null && !Utils.compareTasks(currentTask, fileTask)) {
                            bufferedWriterBuffer.write(toCsvFileFormat(currentTask));
                        } else {
                            // got no tasks, rewrite the current line
                            bufferedWriterBuffer.write(line);
                        }
                        count++;
                        bufferedWriterBuffer.newLine();
                    } else {
                        // if no history?
                        bufferedWriterBuffer.write(line);
                        bufferedWriterBuffer.newLine();
                    }
                }

            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке файла: " + e.getMessage());
        }

        // Копирование буфера в основной файл
        try {
            Files.copy(fileBufferPath, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
        throw new RuntimeException("Ошибка при загрузке или копировании файла: " + e.getMessage());
        }
    }

    public static String toCsvFileFormat(Task task){
        String id = task.getTaskId().toString();
        String title = task.getTaskTitle();
        String description = task.getDescription();
        String status = task.getStatus().toString();
        String type = task.getClass().getSimpleName();
        String epic = type.equals("Subtask") ? ((Subtask) task).getParentId().toString() : "";
        return (String.format("%s,%s,%s,%s,%s,%s", id, type, title, status, description, epic ));

    }

    // no need.. cuz task is a reference
    public static Task fromCsvToTaskFormat(String line){


        String[] data;
        data = line.trim().split(",");
        Task task;

        if (data.length < 5) return null;

        Long taskId = Long.parseLong(data[0].trim());
        String type = data[1].trim();
        String title = data[2].trim();
        Status status = parseStatus(data[3].trim());
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

        return task;
    }

}
