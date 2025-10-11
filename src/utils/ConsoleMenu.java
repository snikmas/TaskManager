package utils;

import manager.InMemoryTaskManager;
import manager.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.Scanner;

public class ConsoleMenu {


    Scanner scanner = new Scanner(System.in);
    InMemoryTaskManager taskManager = new InMemoryTaskManager();


    public void printMenu() {
        System.out.println("==== Task Manager Menu ====");
        System.out.println("[1] Add Task");
        System.out.println("[2] Update Task");
        System.out.println("[3] Find Task by ID");
        System.out.println("[4] View Tasks");
        System.out.println("[5] Delete Task By ID");
        System.out.println("[6] Delete All Tasks");
        System.out.println("[7] Get Epic's subtasks");
        System.out.println("[0] Exit");
    }


    public void start(){
        boolean running = true;

        while(running){
            printMenu();

            int option = -1;
            while(true){
                String userInput = scanner.nextLine();

                try{
                    option = Integer.parseInt(userInput);
                    if(option < 0 || option > 7){
                        System.out.println("Invalid option");
                        continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid option!");
                }
            }


            // 1 add
            // 2 update
            // 3 find by id
            // 4 view all
            // 5 delete by id
            // 6 delete all
            // 7 get epics subtasks
            // 0 exit

            switch (option){
                case 1 -> createTaskPreFunc();
                case 2 -> updateTaskPreFunc();
                case 3 -> findTask();
                case 4 -> viewTasks();
                case 5 -> deleteTask();
                case 6 -> deleteTasks();
                case 7 -> showEpicSubtasks();
                case 0 -> {
                    System.out.println("See you next time!");
                    running = false;
                }
            }


        }
    }


    void createTaskPreFunc(){

        System.out.println("What kind of the task would you like to add?");
        System.out.println("[1] Task");
        System.out.println("[2] Epic");
        System.out.println("[3] Subtasks");

        int option = -1;
        while(true){
            String userInput = scanner.nextLine();

            try{
                option = Integer.parseInt(userInput);
                if(option < 0 || option > 3){
                    System.out.println("Invalid option");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid option!");
            }
        }

        Task newTask;
        switch (option){
            case 1 -> newTask = new Task();
            case 2 -> newTask = new Epic();
            case 3 -> newTask = new Subtask();
            // otherwise next code won't want run
            default -> throw new IllegalArgumentException("Invalid option");
        }

        System.out.println("Task Name:");
        newTask.setTaskName(scanner.nextLine());
        System.out.println("Task Description:");
        newTask.setTaskDesc(scanner.nextLine());
        newTask.setTaskStatus(Status.NEW);

        if(newTask instanceof Subtask){
            System.out.println("Epic's ID:");

            Long parentId = Long.valueOf(scanner.nextLine());

            while (!taskManager.checkParentId(parentId)) {  // через объект
                System.out.println("Please enter a valid ID!");
            }


            ((Subtask) newTask).setParentId(parentId);

            // maybe implement in the utils this funciotn?
            Task parentTask = taskManager.getTaskById(parentId);
            if(parentTask.getTaskStatus() == Status.DONE){
                parentTask.setTaskStatus(Status.IN_PROGRESS);
            }
        }


        taskManager.createTask(newTask);

        System.out.println("Task Successfully created!");

    }

    void updateTaskPreFunc(){

    }

    void findTask(){

    }

    void viewTasks(){

    }

    void deleteTask(){

    }

    void deleteTasks(){

    }

    void showEpicSubtasks(){

    }


}
