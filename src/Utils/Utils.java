package Utils;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

import java.util.Scanner;

public abstract class Utils {

    static Scanner scanner = new Scanner(System.in);

    public static void showMenu(){
        String[] menu = {
                ". Create a new task",
                ". Update a task",
                ". Get a task by id",
                ". Get All Tasks",
                ". Get Epic's subtasks",
                ". Delete All Tasks",
                ". Delete a task by id",
                ". Get History",
                ". Save tasks",
        };

        for(int i = 1; i <= menu.length; i++){
            System.out.println(i + menu[i]);
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
            input = scanner.nextLine();
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

}
