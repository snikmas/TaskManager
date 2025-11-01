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

    // only for epic?
    public static int updateProperty(String type){
        System.out.println("What would you like to update?");
        System.out.println("1. Title");
        System.out.println("2. Description");
        System.out.println("3. Duration");
        System.out.println("4. Start Time");
        if(!type.equals("Epic")){
            System.out.println("5. Status");
            return getInput(5,  false);
        } else {
            return getInput(4, false);
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

    public static String outputTaskInfo(Task task) {
        if (task == null) {
            return null;
        }
        StringBuilder fullString = new StringBuilder("\nTask Info:");
        fullString.append("\n    TaskId: ").append(task.getTaskId() != null ? task.getTaskId() : "Invalid ID");
        fullString.append("\n    Title: ").append(task.getTaskTitle() != null ? task.getTaskTitle() : "Invalid Title");
        fullString.append("\n    Type: ").append(task.getClass().getSimpleName());
        fullString.append("\n    Description: ").append(task.getDescription());
        fullString.append("\n    Status: ").append(task.getStatus());
        fullString.append("\n    Start time: ").append(task.getStartDateTime());
        fullString.append("\n    Duration: ").append(formatDurationPeriod(task.getDuration(), task.getPeriod()));
        fullString.append("\n    End time: ").append(task.getEndDateTime());

        if (task instanceof Epic) {
            List<Subtask> subtasks = ((Epic) task).getSubtaskList();
            fullString.append("\n    Subtasks:");
            if (subtasks.isEmpty()) {
                fullString.append("\n        [No subtasks]");
            } else {
                int counter = 1;
                for (Subtask subtask : subtasks) {
                    fullString.append("\n        ").append(counter).append(". ").append(subtask.getTaskTitle());
                    fullString.append("\n            Description: ").append(subtask.getDescription());
                    fullString.append("\n            Status: ").append(subtask.getStatus());
                    fullString.append("\n            Start Time: ").append(formatDateTime(subtask.getStartDateTime()));
                    fullString.append("\n            Duration:").append(formatDurationPeriod(subtask.getDuration(), subtask.getPeriod()));
                    fullString.append("\n            End Time: ").append(formatDateTime(subtask.getEndDateTime()));
                    counter++;
                }
            }
        } else if (task instanceof Subtask) {
            fullString.append("\n    Belongs to: ").append(((Subtask) task).getParentId());
        }
        // for output
//        System.out.println(fullString.toString());

        // for servers
        return fullString.toString();
    }

    public static String outputAllTasks(List<Task> allTasks) {
        StringBuilder string = new StringBuilder();
        if (allTasks.isEmpty()) {
            System.out.println("No tasks!.");
            string.append("No tasks!");
            return string.toString();
        }
        int counter = 1;
        for (Task task : allTasks) {
            if (task == null) {
                continue;
            }
            System.out.println(counter + ". ");
            string.append(counter).append(". ").append(outputTaskInfo(task)).append("\n");
            outputTaskInfo(task);
            System.out.println();
            counter++;
        }

        return string.toString();
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


        return inputtedDataTime;
    }

//        String regex = "^(\\d{4}|),(\\d{2}|),(\\d{2}|),(\\d{2}|),(\\d{2}|),(\\d{2}|)";
    public static LocalDateTime checkDataInput(String userInput, LocalDateTime timeNow){
        // time is optionally
        if(!Pattern.matches("^(\\d{4})-(\\d{2})-(\\d{2})(?: (\\d{2})(?:-(\\d{2}))?)?$", userInput)) {
            System.out.println("Invalid format! Please, try [YYYY-MM-DD HH-MM]");
            return null;
        }

            DateTimeFormatter formatterWithHours = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime userInputDateTime;

            try {
                if(userInput.contains(" ")){
                    userInputDateTime = LocalDateTime.parse(userInput.trim(), formatterWithHours);
                } else {
                    LocalDate dateOnly = LocalDate.parse(userInput.trim(), formatter);
                    userInputDateTime = dateOnly.atStartOfDay();

                }
            } catch (Exception e1) {
                // the 1st throws exc, try this one
                System.out.println("Invalid Input! Try again (error in checkDataInput e2");
                return null;

            }


        if(userInputDateTime.isBefore(timeNow)){
            System.out.println("Error! Invalid date: the time has already passed");
            return null;
        }

        return userInputDateTime;
    }

//    System.out.println("Duration: [YYYY,MM,DD,HH,MM OR YYYY,MM,DD OR HH,MM]");
    public static Matcher getInputDurationPeriod() {

        String userInput = scanner.nextLine().trim();

        String regex = "^(\\d{4})(?:,(\\d{2}))?(?:,(\\d{2}))?(?:,(\\d{2}))?(?:,(\\d{2}))?$"
            + "|^(\\d{2})(?:,(\\d{2}))?$";

        Pattern pattern = Pattern.compile(regex);

        while (!pattern.matcher(userInput).matches()) {
            System.out.println("Invalid Input! Try again (Format: [YYYY,MM,DD,HH,MM OR YYYY,MM,DD OR HH,MM])");
            userInput = scanner.nextLine().trim();
        }

        Matcher m = pattern.matcher(userInput);
        m.matches();               // <-- IMPORTANT: make the groups available
        return m;
    }

    public static Duration getMatcherDuration(Matcher matcher) {
        long hours   = 0L;
        long minutes = 0L;

        if (matcher.group(4) != null) {
            hours = Long.parseLong(matcher.group(4));
            if (matcher.group(5) != null) {
                minutes = Long.parseLong(matcher.group(5));
            }
        }

        else if (matcher.group(6) != null) {
            hours = Long.parseLong(matcher.group(6));
            if (matcher.group(7) != null) {
                minutes = Long.parseLong(matcher.group(7));
            }
        }

        return Duration.ofHours(hours).plusMinutes(minutes);
    }

    public static Period getMatcherPeriod(Matcher matcher) {
        int years   = 0;
        long months = 0L;
        long days   = 0L;

        if (matcher.group(1) != null) {
            years = Integer.parseInt(matcher.group(1));
            if (matcher.group(2) != null) months = Long.parseLong(matcher.group(2));
            if (matcher.group(3) != null) days   = Long.parseLong(matcher.group(3));
        }

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

    public static String formatDateTime(LocalDateTime dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return dateTime.format(formatter);
    }

    public static String formatDurationPeriod(Duration duration, Period period){
        StringBuilder output = new StringBuilder();
        // later has to check if time is 0
        if(period != null){
            output.append(period.getYears()).append("/");
            output.append(period.getMonths()).append("/");
            output.append(period.getDays()).append(" ");
        }

        if(duration != null){
            output.append(duration.toHours()).append(":");
            output.append(duration.toMinutes());
        }

        return output.toString();
    }


}
