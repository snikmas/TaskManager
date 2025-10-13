package manager;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;
import utils.Utilities;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    public Queue<Task> historyList = new ArrayDeque<>();

    Long taskId = 1L;
    Scanner scanner = new Scanner(System.in);
    private Map<Long, Task> tasks = new HashMap<Long, Task>();




    @Override
    public void createTask(Task task){

        tasks.put(task.getTaskId(), task);
        return;
    };

    @Override
    public void viewAllTasks(){
        if(tasks.isEmpty()){
            System.out.println("No tasks have been added yet.");
        }

        for(Task task : tasks.values()){
            System.out.println(task.toString() + "\n");
            historyList.add(task);

            if(task instanceof Subtask){
                System.out.println("   Belongs to: " + tasks.get(((Subtask) task).getTaskId()).getTaskId());
            } else if(task instanceof Epic epic){
                if(epic.getSubtasks().isEmpty()){
                    break;
                }
                System.out.println("Related Subtasks:");
                for(int ii = 0; ii < epic.getSubtasks().size(); ii++){
                    Subtask subtask = epic.getSubtasks().get(ii);
                    System.out.println((ii + 1) + ".Task ID: " + subtask.getTaskId());
                    System.out.println("   Task Name: " + subtask.getTaskName() + "\n");
                }
            }
        }
    };

    @Override
    public void deleteAllTasks(){
        tasks.clear();
    };


    // utility
    public Task getTaskById(Long taskId){
        return tasks.get(taskId);
    };


    // actuall
    @Override
    public void viewTask(Task task){
        System.out.println("Info:\n" + task.toString());
        addHitstory(task);

    }


    @Override
    public void updateTask(Task task){

        System.out.println("""
                What would you like to update?
                [1] Name
                [2] Description
                [3] Status""");

        int option = -1;
        option = scanner.nextInt();

        while(option < 1 || option > 3){
            System.out.println("Please enter a valid option!");
            option = scanner.nextInt();
        }

        // 1 - name // 2 -> descr// 3-> option
        switch (option){
            case 1 -> {
                System.out.println("Input a new task name:");
                String taskName = scanner.next();
                while(taskName.isBlank()){
                    System.out.println("Invalid task name!");
                    taskName = scanner.next();
                }

                task.setTaskName(taskName.trim());
                return;
            }
            case 2 -> {
                System.out.println("Input a new description:");
                String description = scanner.next();
                while(description.isBlank()){
                    System.out.println("Invalid description!");
                    description = scanner.next();
                }

                task.setTaskDesc(description.trim());
                return;
            }

            // update status
            case 3 -> {

                if(task.getTaskType().equals("Epic")) {
                    System.out.println("You can update Epic's status only by updating its subtasks!");
                    return;

                }
                System.out.println("Status Update\nAvailable Options:");
                // utils options to output stastus
                Utilities.statusMenu();

                int statusOption = -1;
                statusOption = scanner.nextInt();
                while(statusOption < 1 || statusOption > 3){
                    System.out.println("Please enter a valid option!");
                    statusOption = scanner.nextInt();
                }
                switch (statusOption){
                    case 1 -> {
                        task.setTaskStatus(Status.NEW);
                    }
                    case 2 -> {
                        task.setTaskStatus(Status.IN_PROGRESS);
                    }
                    case 3 -> {
                        task.setTaskStatus(Status.DONE);
                    }
                }

                // if can change only subtasks and tasks status, if its subtasks, jave to change epic's status
                if(task.getTaskType().equals("Subtask")){
                    checkEpicStatus(((Subtask)task).getParentId());
                }

            }
        }



        return;
    };

    @Override
    public void deleteById(Long taskId){
        tasks.remove(taskId);
    };

    @Override
    public void getEpicSubtasks(Long epicId){
        if(tasks.isEmpty()){
            System.out.println("No tasks have been added yet.");
        } else {
            Epic task = (Epic) tasks.get(epicId);
            for(Subtask subtask : task.getSubtasks()){
                System.out.println(subtask.toString() + '\n');
            }
        }
    };

    @Override
    public boolean checkParentId(Long parentId){
        return tasks.containsKey(parentId);
    }

     public Long generatorTaskId(){
        return taskId++;
    }

    public boolean epicAvailable(){
        for(Task task : tasks.values()){
            if(task instanceof Epic){
                return true;
            }
        }
        return false;
    }

    protected void checkEpicStatus(Long epicId){
        Epic epic = (Epic)tasks.get(epicId);
        boolean allNew = true;
        boolean allDone = true;

        Status epicStatus = epic.getTaskStatus();
        for(Subtask subtask : epic.getSubtasks()) {

            Status subStatus = subtask.getTaskStatus();

            if (subStatus == Status.IN_PROGRESS) {
                epic.setTaskStatus(Status.IN_PROGRESS);
                return;
            }

            if (subStatus == Status.DONE) {
                allNew = false;
            }
            if (subStatus == Status.NEW) {
                allDone = false;
            }
        }

        if(allDone) {
            epic.setTaskStatus(Status.DONE);
        } else if(allNew) {
            epic.setTaskStatus(Status.NEW);
        } else {
            epic.setTaskStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public void history(){
        System.out.println("History:");
        for(Task task : tasks.values()){
            System.out.println(task.toString() + "\n");
        }
    }

    // util
    public void addHitstory(Task task){
        while(historyList.size() >= 10){
            historyList.poll();
        }
        historyList.add(task);
    }


    public Map<Long, Task> getTasks() {
        return tasks;
    }
    public void setTasks(Map<Long, Task> tasks) {
        this.tasks = tasks;
    }



}
