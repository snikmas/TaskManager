import Managers.Interfaces.HistoryManager;
import Managers.Interfaces.TaskManager;
import Managers.Managers;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import Utils.Utils;

public class Main {

    static TaskManager taskManager = Managers.getDefault();
    static HistoryManager historyManager = Managers.getDefaultHistory();
    static TaskManager fileManager = Managers.getDefaultFileBackendManager();

    public static void main(String[] args) {



        System.out.println("Hi, how are you today?");
        System.out.println("Menu:");
        Utils.showMenu();
        System.out.println(">> ");

        // 9 -> range of nubmers
        int input = Utils.getInput(9, true);

        switch (input){
            case 0 -> {
                System.out.println("See you next time!");
            }
            case 1 -> {
                int taskType = Utils.chooseTypeTask();
                switch(taskType){
                    case 1 -> {
                        Task task = new Task();
                        taskManager.createTask(task);
                    }
                    case 2 -> {
                        if(taskManager.getEpics().isEmpty()){
                            System.out.println("No epics yet. \nBacking to the menu...");
                        } else {
                            Subtask subtask = new Subtask();
                            taskManager.createTask(subtask);
                        }
                    }
                    case 3 -> {
                        Epic epic = new Epic();
                        taskManager.createTask(epic);
                    }
                }
                System.out.println("Created!");
            }
            case 3 -> {

            }
            case 4 ->{

            }
            case 5 -> {}
            case 6 -> {}
            case 7 -> {}
            case 8 -> {}
            case 9 -> {}
        }


    }



}