package Utils;

import Tasks.Epic;
import Tasks.Status;
import Tasks.Subtask;
import Tasks.Task;

import java.util.List;
import java.util.Scanner;

public abstract class Utils {

    static Scanner scanner = new Scanner(System.in);

    public static void showMenu(){
        String[] menu = {
                ". Create a new task", // +
                ". Update a task", // +
                ". Get a task by id", //
                ". Get All Tasks", //
                ". Get Epic's subtasks", //
                ". Delete All Tasks", //
                ". Delete a task by id", //
                ". Get History", //
                ". Save tasks", //
        };

        for(int i = 0; i < menu.length; i++){
            System.out.println((i + 1) + menu[i]);
        }
        System.out.println("0. Exit");
    }

    public static int getInput(int n, boolean isIncludeZero){

        String input = "";
        int number;

        while (true){
            input = scanner.nextLine();
            try {
                number = Integer.parseInt(input);
                if(number > n || (isIncludeZero && number == 0)){
                    System.out.println("Invalid input, try again!");
                    continue;
                } else {
                    return number;
                }
            } catch(NumberFormatException e){
                System.out.println("Invalid input, try again!");
                continue;
            }
        }
    }

    public static String getInput(){
        String input = "";
        while(true){
            if(scanner.hasNextLine()){
                return scanner.nextLine();
            } else{
                System.out.println("You have to input something!");
            }
        }
    }

    public static long getLongInput(){
        String input = "";
        long number;

        while (true){
            input = scanner.nextLine();
            try {
                number = Integer.parseInt(input);
                return number;
            } catch(NumberFormatException e){
                System.out.println("Invalid input, try again!");
            }
        }
    }

    // later can write in one function all getFunctions
    public static int getIntInput(){
        String input = "";
        int number;

        while (true){
            input = scanner.nextLine();
            try {
                number = Integer.parseInt(input);
                return number;
            } catch(NumberFormatException e){
                System.out.println("Invalid input, try again!");
            }
        }
    }

    public static int chooseTypeTask(){

        String[] taskTypes = {". Task", ". Subtask", ". Epic"};

        System.out.println("Choose the task type:");
        System.out.println("1. Task");
        System.out.println("2. Subtask");
        System.out.println("3. Epic");
        return getInput(3, false);
    }

    // later can add more functionality
    public static void changeEpicStatus(Epic epic){
        if(epic.getStatus() == Status.NEW) return;
        epic.setStatus(Status.IN_PROGRESS);
    }

    public static int updateProperty(String type){
        System.out.println("What would you like to update?");
        System.out.println("1. Title");
        System.out.println("2. Description");
        if(!type.equals("Epic")){
            System.out.println("3. Status");
            return getInput(3,  false);
        } else {
            return getInput(2, false);
        }

        // can't change epic's status
    }

    public static Status getStatus(){
        int counter = 1;
        for(Status status : Status.values()){
            System.out.println(counter + ". " + status);
            counter++;
        }
        int input = getInput(3, false);
        switch(input){
            case 1 -> {
                return Status.NEW;
            }
            case 2 -> {
                return Status.IN_PROGRESS;
            }
            case 3 -> {
                return Status.DONE;
            }
        }
        // shouldn't be get
        return null;
    }

    public static void outputTaskInfo(Task task){

        System.out.println("Task Info:");
        System.out.println("TaskId: " + task.getTaskTitle());
        System.out.println("Title: " + task.getTaskTitle());
        System.out.println("Type: " + task.getClass().getSimpleName());
        System.out.println("Description: " + task.getDescription());
        System.out.println("Status: " + task.getStatus());

        if(task instanceof Epic){
            int counter = 1;
            System.out.println("Subtasks:");
            for(Subtask subtask : ((Epic) task).getSubtaskList()){
                System.out.println(counter + ". " + subtask.getTaskTitle());
                counter++;
            }
        } else if(task instanceof Subtask){
            System.out.println("Belongs to: " + ((Subtask) task).getParentId());
        }
    }

    public static void outputAllTasks(List<Task> allTasks) {
        int counter = 1;
        for (Task task : allTasks) {
            System.out.println(counter + ". " + task.getTaskTitle());
            counter++;
        }
    }

    public static void outputAllSubtasks(List<Subtask> allTasks){
        int counter = 1;
        for(Task task : allTasks){
            System.out.println(counter + ". " + task.getTaskTitle());
            counter++;
        }
    }
}
