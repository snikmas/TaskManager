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
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    String firstLine = "id,type,title,status,description,epic,startTime,durationTime,endTime";
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


        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath.toFile()));
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath.toFile()))){
                bufferedWriter.write(firstLine);
                bufferedWriter.newLine();

                if(getAllTypesTasks().isEmpty()) {
                    //
                    System.out.println(getAllTypesTasks().values());
                    System.out.println("There're no tasks!");
                    return;
                };
                String line;
                for(Task task : getAllTypesTasks().values()) {
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

        HistoryManager historyManager = getDefaultHistory();

        System.out.println("Loading from file: " + filePath.toAbsolutePath());
        if (!Files.exists(filePath)) {
            System.err.println("File does not exist: " + filePath);
            return;
        }

        // can this things delete my file?
        // Clear existing tasks and history
//        Managers.getDefault().getAllTypesTasks().clear();
//        getTasks().clear();
//        getSubtasks().clear();
//        getEpics().clear();
//        historyManager.getHistory().clear();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath.toFile()));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath.toFile(), true))) {
            // Skip the header line

            String line = bufferedReader.readLine();
            if (line == null || !line.equals(firstLine)) {
                bufferedWriter.write(firstLine);
                bufferedWriter.newLine();
            }

            // Load tasks
            int taskCount = 0;
            while ((line = bufferedReader.readLine()) != null && !line.equals("History:")) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                Task task = fromCsvToTaskFormat(line);
                if (task == null) {
                    continue;
                }

                // from the csv
                this.getAllTypesTasks().put(task.getTaskId(), task);
                if (task instanceof Task && !(task instanceof Subtask || task instanceof Epic)) {
                    this.getTasks().put(task.getTaskId(), task);
                } else if (task instanceof Subtask) {
                    this.getSubtasks().put(task.getTaskId(), (Subtask) task);
                } else if (task instanceof Epic) {
                    this.getEpics().put(task.getTaskId(), (Epic) task);
                }
                taskCount++;
            }

            // Load history
            if (line != null && line.equals("History:")) {
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.trim().isEmpty()) {
                        continue;
                    }
                    String csvData = line.replaceFirst("^\\d+\\.", "").trim();
                    Task task = fromCsvToTaskFormat(csvData);
                    System.out.println("history task sdave: " + task);
                    if (task != null && Managers.getDefault().getAllTypesTasks().containsKey(task.getTaskId())) {
                        historyManager.add(task);
                    } else {
                        System.err.println("Missing task: " + line);
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to load: " + filePath + ": " + e.getMessage(), e);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid data format: " + filePath + ": " + e.getMessage(), e);
        }

        // Update task ID generator
        long maxId = Managers.getDefault().getAllTypesTasks().keySet().stream().mapToLong(Long::longValue).max().orElse(0L);
        InMemoryTaskManager.id = maxId + 1;
        for(Task task : Managers.getDefault().getAllTypesTasks().values()){
            if(task == null) {
                System.out.println("no taskss."); return;
            }
        }
    }

    public static String toCsvFileFormat(Task task){
        String id = task.getTaskId().toString();
        String title = task.getTaskTitle();
        String description = task.getDescription();
        String status = task.getStatus().toString();
        String type = task.getClass().getSimpleName();
        String epic = type.equals("Subtask") ? ((Subtask) task).getParentId().toString() : "";
        String startTime = task.getStartDateTime().toString();
        String durationTIme = task.getDuration().toString();
        String endTime = task.getEndDateTime().toString();
        return (String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s", id, type, title, status, description, epic, startTime, durationTIme, endTime));
//        String firstLine = "id,type,title,status,description,epic,startTime,durationTime,endTime";
    }

    public static Task fromCsvToTaskFormat(String line){


        String[] data;
        data = line.trim().split(",");
        Task task;

        if (data.length < 5) return null;

        int i = 0;
        String epicId;
        Long taskId = Long.parseLong(data[i++].trim());
        String type = data[i++].trim();
        String title = data[i++].trim();
        Status status = parseStatus(data[i++].trim());
        String description = data[i++].trim();
        if(data[i].isEmpty()) i++;
        // case if it has an epic?

        String startTime = data[i++].trim();
        String durationTime = data[i++].trim();
        String endTime = data[i++].trim();


        switch (type) {
            case "Subtask" -> {
                task = new Subtask();
                if (data.length > 5) {
                    epicId = data[5].trim();
                    ((Subtask) task).setParentId(Long.parseLong(epicId));
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

        try {
            task.setStartDateTime(LocalDateTime.parse(startTime));
        } catch (Exception e1){
            try {
                task.setStartDateTime(LocalDate.parse(startTime).atStartOfDay());
            } catch (Exception e2){
                System.out.println("from csv to task fromat, end, still wrong");
            }
        }

        task.setDuration(Duration.parse(durationTime));


        try {
            task.setEndDateTime(LocalDateTime.parse(endTime));
        } catch (Exception e1){
            try {
                task.setEndDateTime(LocalDate.parse(endTime).atStartOfDay());
            } catch (Exception e2){
                System.out.println("from csv to task fromat, end, still wrong");
            }

        }

        return task;
    }
}
