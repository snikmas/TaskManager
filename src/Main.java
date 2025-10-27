import Managers.Interfaces.HistoryManager;
import Managers.Interfaces.TaskManager;
import Managers.FileBackendTaskManager;
import Managers.Managers;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import Utils.Utils;

import java.util.List;

public class Main {
    static FileBackendTaskManager fileManager = Managers.getDefaultFileBackendManager();
    static HistoryManager historyManager = Managers.getDefaultHistory();

    public static void main(String[] args) {
        fileManager.load();
        int input = -1;
        System.out.println("Hi, how are you today?");
        while (input != 0) {
            System.out.println("Menu:");
            Utils.showMenu();
            System.out.print(">> ");
            input = Utils.getInput(9, true);

            switch (input) {
                case 0 -> System.out.println("See you next time!");
                case 1 -> {
                    int taskType = Utils.chooseTypeTask();
                    switch (taskType) {
                        case 1 -> {
                            Task task = new Task();
                            fileManager.createTask(task);
                            System.out.println("Created!");
                        }
                        case 2 -> {
                            if (fileManager.getEpics().isEmpty()) {
                                System.out.println("No epics yet. \nBacking to the menu...");
                            } else {
                                Subtask subtask = new Subtask();
                                fileManager.createTask(subtask);
                                System.out.println("Created!");
                            }
                        }
                        case 3 -> {
                            Epic epic = new Epic();
                            fileManager.createTask(epic);
                            System.out.println("Created!");
                        }
                    }
                }
                case 2 -> {
                    System.out.println("Input a task ID:");
                    Long taskId = Utils.getLongInput();
                    while (true) {
                        if (fileManager.getTasks().containsKey(taskId)) {
                            fileManager.updateTask(fileManager.getTasks().get(taskId), taskId);
                            break;
                        } else if (fileManager.getSubtasks().containsKey(taskId)) {
                            fileManager.updateTask(fileManager.getSubtasks().get(taskId), taskId);
                            break;
                        } else if (fileManager.getEpics().containsKey(taskId)) {
                            Epic epic = (Epic) fileManager.getEpics().get(taskId);
                            input = Utils.updateProperty("Epic");
                            if (input == 0) break;
                            fileManager.updateTask(epic, taskId);
                            break;
                        } else {
                            System.out.println("Task hasn't been found. Try again (0 to exit)");
                            taskId = Utils.getLongInput();
                            if (taskId == 0) break;
                        }
                    }
                    if (taskId != 0) System.out.println("The task has been updated!\nBacking to the menu...");
                }
                case 3 -> {
                    System.out.println("Input a task Id:");
                    Long taskId = Utils.getLongInput();
                    while (!fileManager.getAllTypesTasks().containsKey(taskId)) {
                        System.out.println("Invalid input! Try again...");
                        taskId = Utils.getLongInput();
                    }
                    Task task = fileManager.getTaskById(taskId);
                    historyManager.add(task);
                    if (task instanceof Subtask subtask) {
                        Utils.outputTaskInfo(subtask);
                    } else if (task instanceof Epic epic) {
                        Utils.outputTaskInfo(epic);
                    } else {
                        Utils.outputTaskInfo(task);
                    }
                    System.out.println("Backing to the menu...\n");
                }
                case 4 -> {
                    System.out.println("All tasks:");
                    List<Task> allTasks = fileManager.getAllTasks();
                    for (Task task : allTasks) {
                        historyManager.add(task);
                    }
                    Utils.outputAllTasks(allTasks);
                }
                case 5 -> {
                    System.out.println("Input Epic's id:");
                    Long taskId = Utils.getLongInput();
                    while (!fileManager.getEpics().containsKey(taskId)) {
                        System.out.println("Invalid input! Try again...");
                        taskId = Utils.getLongInput();
                    }
                    Epic epic = (Epic) fileManager.getTaskById(taskId);
                    List<Subtask> result = fileManager.getEpicSubtasks(epic);
                    for (Subtask subtask : result) {
                        historyManager.add(subtask);
                    }
                    System.out.println("Result:");
                    Utils.outputAllSubtasks(result);
                }
                case 6 -> {
                    System.out.println("Are you sure?");
                    System.out.println("1. Yes");
                    System.out.println("2. No");
                    int userInput = Utils.getInput(2, false);
                    if (userInput == 1) {
                        fileManager.deleteAllTasks();
                        System.out.println("Tasks has been deleted.");
                    }
                    System.out.println("Back to the menu...");
                }
                case 7 -> {
                    System.out.println("Input the task's id:");
                    Long userInput = Utils.getLongInput();
                    while (!fileManager.getAllTypesTasks().containsKey(userInput)) {
                        System.out.println("Invalid input! Try again...");
                        userInput = Utils.getLongInput();
                    }
                    fileManager.deleteTaskById(userInput);
                    historyManager.delete(userInput);
                    System.out.println("The task has been deleted!");
                }
                case 8 -> {
                    List<Task> history = fileManager.history();
                    System.out.println("History:");
                    Utils.outputAllTasks(history);
                }
                case 9 -> fileManager.save();
            }
        }
    }
}