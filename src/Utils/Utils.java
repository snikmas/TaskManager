package Utils;

import Tasks.Epic;
import Tasks.Status;
import Tasks.Subtask;
import Tasks.Task;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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

    public static String getInput() {
        String input = scanner.nextLine();
        while (input.trim().isEmpty()) {
            System.out.println("Input cannot be empty! Try again:");
            input = scanner.nextLine();
        }
        return input;
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

    public static void outputTaskInfo(Task task) {
        if (task == null) {
            return;
        }
        System.out.println("Task Info:");
        System.out.println("    TaskId: " + (task.getTaskId() != null ? task.getTaskId() : "Invalid ID"));
        System.out.println("    Title: " + (task.getTaskTitle() != null ? task.getTaskTitle() : "Invalid Title"));
        System.out.println("    Type: " + task.getClass().getSimpleName());
        System.out.println("    Description: " + task.getDescription());
        System.out.println("    Status: " + task.getStatus());

        if (task instanceof Epic) {
            List<Subtask> subtasks = ((Epic) task).getSubtaskList();
            System.out.println("    Subtasks:");
            if (subtasks.isEmpty()) {
                System.out.println("        [No subtasks]");
            } else {
                int counter = 1;
                for (Subtask subtask : subtasks) {
                    System.out.println("        " + counter + ". " + subtask.getTaskTitle());
                    System.out.println("            Description: " + subtask.getDescription());
                    System.out.println("            Status: " + subtask.getStatus());
                    counter++;
                }
            }
        } else if (task instanceof Subtask) {
            System.out.println("    Belongs to: " + ((Subtask) task).getParentId());
        }
    }

    public static void outputAllTasks(List<Task> allTasks) {
        if (allTasks.isEmpty()) {
            System.out.println("No tasks!.");
            return;
        }
        int counter = 1;
        for (Task task : allTasks) {
            if (task == null) {
                continue;
            }
            System.out.println(counter + ". ");
            outputTaskInfo(task);
            System.out.println();
            counter++;
        }
    }

    public static void outputAllSubtasks(List<Subtask> allTasks){
        int counter = 1;
        for(Task task : allTasks){
            System.out.println(counter + ". ");
            outputTaskInfo(task);
            System.out.println("\n");
            counter++;
        }
    }

    public static Status parseStatus(String status){
        switch (status) {
            case "NEW" -> {
                return Status.NEW;
            }
            case "IN_PROGRESS" -> {
                return Status.IN_PROGRESS;
            }
            case "DONE" -> {
                return Status.DONE;
            }
        }
        return null;
    }

    // year / mm / d
    public static LocalDateTime getInputTime(){

        // mm for minutes; MM for months

        String userInput = scanner.nextLine();
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime inputtedDataTime;
        while(((inputtedDataTime = checkDataInput(userInput, currentDateTime)) == null)){
            userInput = scanner.nextLine();
        }

        System.out.println(inputtedDataTime);

        return inputtedDataTime;
    }

    public static LocalDateTime checkDataInput(String userInput, LocalDateTime timeNow){
        if(!Pattern.matches("\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d-\\d\\d", userInput)) {
            System.out.println("Invalid format! Please, try [YYYY-MM-DD HH-MM]");
            return null;
        }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm");
            LocalDateTime userInputDateTime;
        try {
            userInputDateTime = LocalDateTime.parse(userInput.trim(), formatter);
        } catch (Exception e) {
            System.out.println("Invalid Input! Try again");
            return null;
        }


        if(userInputDateTime.isBefore(timeNow)){
            System.out.println("Error! Invalid date: the time has already passed");
            return null;
        }

        return userInputDateTime;
    }

//    System.out.println("Duration: [YYYY,MM,DD,HH,MM]");
    public static Matcher getInputDurationPeriod() {
        String userInput = scanner.nextLine().trim();
        String regex = "^(\\d{4}|),(\\d{2}|),(\\d{2}|),(\\d{2}|),(\\d{2}|),(\\d{2}|)";

        while (!Pattern.matches(userInput, regex)) {
            System.out.println("Invalid Input! Try again (Format: [YYYY,MM,DD,HH,MM");
            userInput = scanner.nextLine();
        }

        Pattern pattern = Pattern.compile(regex);

        return pattern.matcher(userInput);
    }

    public static Duration getMatcherDuration(Matcher matcher) {
        // to long and from long to duraotion
        long hours = 0L;
        long minutes = 0L;

        if(!matcher.group(4).isEmpty() && matcher.group(4) != null) hours = Long.parseLong(matcher.group(4));
        if(!matcher.group(5).isEmpty() && matcher.group(5) != null) minutes = Long.parseLong(matcher.group(5));
        return Duration.ofHours(hours).plusMinutes(minutes);
    }

    public static Period getMatcherPeriod(Matcher matcher) {

        long months = 0L;
        long days = 0L;
        int years = 0;

        if(!matcher.group(1).isEmpty() && matcher.group(1) != null)  years = Integer.parseInt(matcher.group(1));
        if(!matcher.group(2).isEmpty() && matcher.group(2) != null)  months = Long.parseLong(matcher.group(2));
        if(!matcher.group(3).isEmpty() && matcher.group(3) != null) days = Long.parseLong(matcher.group(3));

        return Period.ofYears(years).plusMonths(months).plusDays(days);
    }

    public static LocalDateTime calculateEndTime(LocalDateTime startTime, Duration duration, Period period){

        long years = period.getYears();
        long month = period.getMonths();
        long days = period.getDays();
        long hours = duration.toHours();
        long minutes = duration.toMinutes();

        return startTime.plusYears(years)
                .plusMonths(month)
                .plusDays(days)
                .plusHours(hours)
                .plusMinutes(minutes);
    }


    public static boolean compareTasks(Task currentTask, Task fileTask) {
        return (
                currentTask.getTaskId() == fileTask.getTaskId())
                && currentTask.getStatus() == fileTask.getStatus()
                && currentTask.getTaskTitle().equals(fileTask.getTaskTitle())
                && currentTask.getDescription().equals(fileTask.getDescription())
                && currentTask.getClass().equals(fileTask.getClass());
    }


}
