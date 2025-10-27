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
import java.util.Map;

import static Managers.Managers.*;
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

    String firstLine = "id,type,title,status,description,epic";
    private final Path filePath;
    private final Path fileBufferPath;
    HistoryManager historyManager = getDefaultHistory();

    // ADD MORE
    // when it starts?
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

        // load current file.. ?
        load();
    }
    // fromat:
    public void save(){
        Map<Long, Task> allTypesTasks = taskManager.getAllTypesTasks();

        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath.toFile()));
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath.toFile()))){
                bufferedWriter.write(firstLine);
                bufferedWriter.newLine();

                if(allTypesTasks.isEmpty()) {
                    System.out.println("There're no tasks!");
                    return;
                };
                String line;
                for(Task task : allTypesTasks.values()) {
                    if (task == null) continue;
                    line = toCsvFileFormat(task);
                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                }

                List<Task> curHistory = historyManager.getHistory();
                if(!curHistory.isEmpty()){
                    bufferedWriter.write("History:");
                    bufferedWriter.newLine();

                    int counter = 1;
                    for(Task task : curHistory){
                        line = toCsvFileFormat(task);
                        bufferedWriter.write(counter + "." + line);
                        bufferedWriter.newLine();
                        counter++;
                    }
                }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }



    }

    // IT WILL LOAD AS IT JUST STARTED WORK, SO NO NEED TO CHECK WITH CURRENT...
    public void load() {
        Map<Long, Task> allTypesTasks = getAllTypesTasks();
        HistoryManager historyManager = getDefaultHistory();

        System.out.println("Loading from file: " + filePath.toAbsolutePath());
        if (!Files.exists(filePath)) {
            System.err.println("File does not exist: " + filePath);
            return;
        }

        // Clear existing tasks and history
        allTypesTasks.clear();
        getTasks().clear();
        getSubtasks().clear();
        getEpics().clear();
        historyManager.getHistory().clear();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath.toFile()))) {
            // Skip the header line
            String line = bufferedReader.readLine();
            if (line == null || !line.equals(firstLine)) {
                throw new RuntimeException("Invalid or missing header in file: " + filePath);
            }
            System.out.println("Header verified: " + line);

            // Load tasks
            int taskCount = 0;
            while ((line = bufferedReader.readLine()) != null && !line.equals("History:")) {
                if (line.trim().isEmpty()) {
                    System.out.println("Skipping empty task line");
                    continue;
                }
                Task task = fromCsvToTaskFormat(line);
                if (task == null) {
                    System.err.println("Skipping invalid task line: " + line);
                    continue;
                }

                allTypesTasks.put(task.getTaskId(), task);
                if (task instanceof Task && !(task instanceof Subtask || task instanceof Epic)) {
                    getTasks().put(task.getTaskId(), (Task) task);
                    System.out.println("Added Task: " + task.getTaskId());
                } else if (task instanceof Subtask) {
                    getSubtasks().put(task.getTaskId(), (Subtask) task);
                    System.out.println("Added Subtask: " + task.getTaskId());
                } else if (task instanceof Epic) {
                    getEpics().put(task.getTaskId(), (Epic) task);
                    System.out.println("Added Epic: " + task.getTaskId());
                }
                taskCount++;
            }
            System.out.println("Loaded " + taskCount + " tasks");

            // Load history
            if (line != null && line.equals("History:")) {
                System.out.println("Loading history section");
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.trim().isEmpty()) {
                        System.out.println("Skipping empty history line");
                        continue;
                    }
                    String csvData = line.replaceFirst("^\\d+\\.", "").trim();
                    Task task = fromCsvToTaskFormat(csvData);
                    if (task != null && allTypesTasks.containsKey(task.getTaskId())) {
                        historyManager.add(task);
                        System.out.println("Added task to history: " + task.getTaskId());
                    } else {
                        System.err.println("Skipping invalid history line or missing task: " + line);
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to load file " + filePath + ": " + e.getMessage(), e);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid data format in file " + filePath + ": " + e.getMessage(), e);
        }

        // Update task ID generator
        long maxId = allTypesTasks.keySet().stream().mapToLong(Long::longValue).max().orElse(0L);
        InMemoryTaskManager.id = maxId + 1;

        // Debug: Print loaded tasks
        System.out.println("Total tasks in allTypesTasks: " + allTypesTasks.size());
        System.out.println("Tasks: " + getTasks().size());
        System.out.println("Subtasks: " + getSubtasks().size());
        System.out.println("Epics: " + getEpics().size());
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
