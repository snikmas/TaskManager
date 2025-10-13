package utils;

import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.Scanner;

public class ConsoleMenu {

    Scanner scanner = new Scanner(System.in);
    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    InMemoryTaskManager taskManager = new InMemoryTaskManager(historyManager);


    public void printMenu() {
        System.out.println("\n==== Task Manager Menu ====");
        System.out.println("[1] Add Task");
        System.out.println("[2] Update Task");
        System.out.println("[3] Find Task by ID");
        System.out.println("[4] View Tasks");
        System.out.println("[5] Delete Task By ID");
        System.out.println("[6] Delete All Tasks");
        System.out.println("[7] Get Epic's subtasks");
        System.out.println("[8] View History");
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
                    if(option < 0 || option > 8){
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
                case 8 -> viewHistory();
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
                if(option < 1 || option > 3){
                    System.out.println("Invalid option");
                    continue;
                } else if (option == 3 && !taskManager.epicAvailable()){
                    System.out.println("Epics haven't been added yet! You can't create a Subtask now");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid option!");
            }
        }

        Task newTask;
        switch (option){
            case 1 -> {
                newTask = new Task();
                newTask.setTaskType("Task");
            }
            case 2 -> {
                newTask = new Epic();
                newTask.setTaskType("Epic");
            }
            case 3 -> {
                newTask = new Subtask();
                newTask.setTaskType("Subtasks");
            }
            // otherwise next code won't want run
            default -> throw new IllegalArgumentException("Invalid option");
        }

        System.out.println("Task Name:");
        newTask.setTaskName(scanner.nextLine());
        System.out.println("Task Description:");
        newTask.setTaskDesc(scanner.nextLine());
        newTask.setTaskStatus(Status.NEW);
        newTask.setTaskId(taskManager.generatorTaskId());

        taskManager.createTask(newTask);

        if(newTask instanceof Subtask){
            System.out.println("Epic's ID:");

            Long parentId = Long.valueOf(scanner.nextLine());

            // later can change implementation: task becomes epics
            while (!taskManager.checkParentId(parentId) || !taskManager.getTaskById(parentId).getTaskType().equals("Epic")) {  // через объект
                System.out.println("Please enter a valid ID!");
                parentId = Long.valueOf(scanner.nextLine());
            }


            // maybe implement in the utils this funciotn?
            Task parentTask = taskManager.getTaskById(parentId);



            ((Subtask) newTask).setParentId(parentId);

            if(parentTask.getTaskStatus() == Status.DONE){
                parentTask.setTaskStatus(Status.IN_PROGRESS);
            }
        } else if(newTask instanceof Epic){
            System.out.println("How many Subtasks would you like to add? (For one time, you can and 1-5 subtasks)");

            int numberOfSubtasks = Integer.parseInt(scanner.nextLine());
            while(numberOfSubtasks < 1 || numberOfSubtasks > 5){
                System.out.println("Enter a valid number!");
                numberOfSubtasks = Integer.parseInt(scanner.nextLine());
            }

            for(int i = 0; i < numberOfSubtasks; i++){
                Subtask newSubtask = new Subtask();
                System.out.println(i + 1 + ". Subtask Name:");
                newSubtask.setTaskName(scanner.nextLine());
                System.out.println("Subtask Description: ");
                newSubtask.setTaskDesc(scanner.nextLine());
                newSubtask.setTaskStatus(Status.NEW);
                newSubtask.setParentId(newTask.getTaskId());
                newSubtask.setTaskType("Subtask");
                newSubtask.setTaskId(taskManager.generatorTaskId());
                taskManager.createTask(newSubtask);

                ((Epic)newTask).getSubtasks().add(newSubtask);
                System.out.println("Subtask created!\n");

            }
        }



        System.out.println("Task Successfully created!");

    }

    void updateTaskPreFunc(){
        // firstly create a task and later push it
        System.out.println("Enter Task ID to update:");
        Long taskId = Long.valueOf(scanner.nextLine());
        while(taskManager.getTaskById(taskId) == null){
            System.out.println("Please enter a valid ID!");
            taskId = Long.valueOf(scanner.nextLine());
        }

        Task task = taskManager.getTaskById(taskId);
        taskManager.updateTask(task);

        System.out.println("Updated!");

    }

    void findTask(){
        System.out.println("Input Task ID:");
        Long taskId = Long.valueOf(scanner.nextLine());
        if(taskManager.getTaskById(taskId) == null){
            System.out.println("Task does not exist!");
        } else {
            taskManager.viewTask(taskManager.getTaskById(taskId));
            historyManager.add(taskManager.getTaskById(taskId));
        }
    }

    void viewTasks(){
        System.out.println("All tasks:");
        taskManager.viewAllTasks();

    }

    void deleteTask(){
        System.out.println("Enter Task ID to delete:");
        Long taskId = Long.valueOf(scanner.nextLine());
        if(taskManager.getTaskById(taskId) == null){
            System.out.println("Task does not exist!");
            taskId = Long.valueOf(scanner.nextLine());
        }

        taskManager.deleteById(taskId);
        System.out.println("Task deleted!");
    }

    void deleteTasks(){
        if(taskManager.getTasks().isEmpty()){
            System.out.println("There are no tasks to delete!");
            return;
        }
        System.out.println("Are you sure you want to delete all" + taskManager.getTasks().size() + "the tasks? [Y/N]");
        String answer = scanner.nextLine();

        while(!answer.equalsIgnoreCase("y") || !answer.equalsIgnoreCase("n")){
            System.out.println("Please enter a valid option!");
            answer = scanner.nextLine();
        }
        if(answer.equalsIgnoreCase("y")){
            deleteTasks();
            System.out.println("All tasks deleted!");
        } else if(answer.equalsIgnoreCase("n")) {
            System.out.println("The operation has been cancelled...");
        }
    }

    void showEpicSubtasks(){
        System.out.println("Input Epic Id");
        Long epicId = Long.valueOf(scanner.nextLine());
        while(taskManager.getTaskById(epicId) == null){
            System.out.println("Task does not exist! Please enter a valid ID!");
            epicId = Long.valueOf(scanner.nextLine());
        }

        taskManager.getEpicSubtasks(epicId);
    }

    void viewHistory(){
        historyManager.getHistory();
    }

}
