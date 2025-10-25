import Managers.Interfaces.HistoryManager;
import Managers.Interfaces.TaskManager;
import Managers.Managers;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import Utils.Utils;

import java.util.List;

public class Main {

    static TaskManager taskManager = Managers.getDefault();
    static HistoryManager historyManager = Managers.getDefaultHistory();
    static TaskManager fileManager = Managers.getDefaultFileBackendManager();

    public static void main(String[] args) {

        int input = -1;
        System.out.println("Hi, how are you today?");
        while (input != 0) {

            System.out.println("Menu:");
            Utils.showMenu();
            System.out.print(">> ");

            // 9 -> range of nubmers
            input = Utils.getInput(9, true);

            switch (input) {
                case 0 -> {
                    System.out.println("See you next time!");
                }
                case 1 -> {
                    int taskType = Utils.chooseTypeTask();
                    switch (taskType) {
                        case 1 -> {
                            Task task = new Task();
                            taskManager.createTask(task);
                            System.out.println("Created!");
                        }
                        case 2 -> {
                            if (taskManager.getEpics().isEmpty()) {
                                System.out.println("No epics yet. \nBacking to the menu...");
                            } else {
                                Subtask subtask = new Subtask();
                                taskManager.createTask(subtask);
                                System.out.println("Created!");
                            }
                        }
                        case 3 -> {
                            Epic epic = new Epic();
                            taskManager.createTask(epic);
                            System.out.println("Created!");
                        }
                    }
                }
                case 2 -> {
                    Long taskId = Utils.getLongInput();
                    while(true){
                        if(taskManager.getTasks().containsKey(taskId)){
                            taskManager.updateTask(taskManager.getTasks().get(taskId), taskId);
                            break;
                        } else if(taskManager.getSubtasks().containsKey(taskId)){
                            taskManager.updateTask(taskManager.getSubtasks().get(taskId), taskId);
                            break;
                        } else if(taskManager.getEpics().containsKey(taskId)){
                            taskManager.updateTask(taskManager.getSubtasks().get(taskId), taskId);
                            break;
                        } else {
                            System.out.println("Task hasn't been found. Try again (0 to exit)");
                            taskId = Utils.getLongInput();
                            if(taskId == 0) break;
                        }
                    }
                    if(taskId != 0) System.out.println("The task has been updated!\nBacking to the menu...");
                }
                case 3 -> {
                    // get by id
                    System.out.println("Input a task Id:");
                    Long taskId = Utils.getLongInput();

                    while(!taskManager.getAllTypesTasks().containsKey(taskId)){
                        System.out.println("Invalid input! Try again...");
                        taskId = Utils.getLongInput();
                    }

                    Task task = taskManager.getTaskById(taskId);
                    System.out.println("Result:");

                    if(task instanceof Subtask subtask){
                        Utils.outputTaskInfo(subtask);
                    } else if(task instanceof Epic epic){
                        Utils.outputTaskInfo(epic);
                    } else {
                        Utils.outputTaskInfo(task);
                    }
                    System.out.println("Backing to the menu...\n");

                }
                // get all tasks
                case 4 -> {
                    System.out.println("All tasks:");
                    List<Task> allTasks = taskManager.getAllTasks();
                    Utils.outputAllTasks(allTasks);
                }
                // get epuc's subtasks
                case 5 -> {
                    System.out.println("Input Epic's id:");
                    Long taskId = Utils.getLongInput();


                    while(!taskManager.getEpics().containsKey(taskId)){
                        System.out.println("Invalid input! Try again...");
                        taskId = Utils.getLongInput();
                    }
                    Epic epic = (Epic) taskManager.getTaskById(taskId);
                    List<Subtask> result = taskManager.getEpicSubtasks(epic);
                    System.out.println("Result:");
                    Utils.outputAllTasks(result);
                    ;

                }
                case 6 -> {
                }
                case 7 -> {
                }
                case 8 -> {
                }
            }


        }
    }


}