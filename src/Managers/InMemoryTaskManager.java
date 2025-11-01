package Managers;
import Tasks.Epic;
import Tasks.Status;
import Utils.Utils;
import Managers.Interfaces.TaskManager;
import Tasks.Subtask;
import Tasks.Task;

import java.util.*;
import java.util.regex.Matcher;

import static Utils.Utils.updateProperty;


// delete abstract
public class InMemoryTaskManager implements TaskManager {


    private HashMap<Long, Task> allTypesTasks = new HashMap<>();
    private HashMap<Long, Task> tasks = new HashMap<>();
    private HashMap<Long, Subtask> subtasks = new HashMap<>();
    private HashMap<Long, Epic> epics = new HashMap<>();
    static Long id = 1L;


    // ================================================
    // =========== CREATING FUNCTIONS =================

    // FOR UNIT TEST OVERLOAD
    public void createTask(Task task, String title, String description){
        System.out.println("Title: ");
        task.setTaskTitle(title);
        System.out.println("Description: ");
        task.setDescription(description);

        task.setStatus(Status.NEW);
        task.setTaskId(id);

        tasks.put(id, task);
        allTypesTasks.put(id, task);
        id++;
    }

    public void createTask(Task task) {

        System.out.println("Title:");
        task.setTaskTitle(Utils.getInput());
        System.out.println("Description:");
        task.setDescription(Utils.getInput());

        System.out.println("Start time: [YYYY-MM-DD HH-MM]");
        task.setStartDateTime(Utils.getInputTime());

        System.out.println("Duration: [YYYY,MM,DD,HH,MM OR YYYY,MM,DD OR HH,MM]");
        Matcher userMatcherInput = Utils.getInputDurationPeriod();

        task.setDuration(Utils.getMatcherDuration(userMatcherInput));
        task.setPeriod(Utils.getMatcherPeriod(userMatcherInput));

        // calculate end time...
        task.setEndDateTime(Utils.calculateEndTime(task.getStartDateTime(), task.getDuration(), task.getPeriod()));

        task.setStatus(Status.NEW);
        task.setTaskId(id);

        tasks.put(id, task);
        allTypesTasks.put(id, task); //?
        id++;
    }

    public void createTask(Subtask subtask) {
        // just fill info
        System.out.println("Title:");
        subtask.setTaskTitle(Utils.getInput());
        System.out.println("Description:");
        subtask.setDescription(Utils.getInput());

        // SET OPTIONALLY + 00 IF NO INPUTS
        System.out.println("Start time: [YYYY-MM-DD HH-MM]");
        subtask.setStartDateTime(Utils.getInputTime());

        System.out.println("Duration: [YYYY,MM,DD,HH,MM]");
        Matcher userMatcherInput = Utils.getInputDurationPeriod();

        subtask.setDuration(Utils.getMatcherDuration(userMatcherInput));
        subtask.setPeriod(Utils.getMatcherPeriod(userMatcherInput));

        // calculate end time...
        subtask.setEndDateTime(Utils.calculateEndTime(subtask.getStartDateTime(), subtask.getDuration(), subtask.getPeriod()));

        // change the start time if..
        System.out.println("Epic Id:");
        while (true) {
            long epicId = Utils.getLongInput();
            if (!epics.containsKey(epicId)) {
                System.out.println("Wrong ID! Try again!");
            } else {
                Epic parentEpic = epics.get(epicId);
                Utils.changeEpicStatus(parentEpic);
                updateTask(parentEpic);
                subtask.setParentId(epicId);
                parentEpic.getSubtaskList().add(subtask);

                while(parentEpic.getStartDateTime().isAfter(subtask.getStartDateTime())){
                    System.out.println("The subtask's Start time should be after it's epic's start time! Try again...");
                    System.out.println("Start time: [YYYY-MM-DD HH-MM]");
                    subtask.setStartDateTime(Utils.getInputTime());
                }

                if(parentEpic.getEndDateTime().isBefore(subtask.getEndDateTime())){
                    parentEpic.setEndDateTime(subtask.getEndDateTime());
                }

                break;
            }
        }

        subtask.setStatus(Status.NEW);
        subtask.setTaskId(id);

        subtasks.put(id, subtask);
        allTypesTasks.put(id, subtask);
        id++;
    }

    // epic's time is calculating by all its subtasks
    public void createTask(Epic epic) {

        // just fill info
        System.out.println("Title:");
        epic.setTaskTitle(Utils.getInput());
        System.out.println("Description:");
        epic.setDescription(Utils.getInput());
        epic.setStatus(Status.NEW);
        epic.setTaskId(id);

        epics.put(id, epic);
        allTypesTasks.put(id, epic);
        id++;

    }




    // ===========================================================
    // ================== UPDATE FUNCTIONS =======================
    public void updateTask(Task task, Long taskId) {

        int input = Utils.updateProperty("Task");
        switch (input) {
            case 1 -> {
                System.out.println("New title:");
                String title = Utils.getInput();
                task.setTaskTitle(title);
            }
            case 2 -> {
                System.out.println("New description:");
                String description = Utils.getInput();
                task.setDescription(description);
            }
            case 3 -> {

                System.out.println("Duration: [YYYY,MM,DD,HH,MM]");
                Matcher userMatcherInput = Utils.getInputDurationPeriod();

                task.setDuration(Utils.getMatcherDuration(userMatcherInput));
                task.setPeriod(Utils.getMatcherPeriod(userMatcherInput));

                // recalculate end time
                task.setEndDateTime(Utils.calculateEndTime(task.getStartDateTime(), task.getDuration(), task.getPeriod()));
                if (task instanceof Subtask subtask){
                    Epic epicParent = epics.get(subtask.getParentId());
                    if(epicParent.getEndDateTime().isBefore(subtask.getEndDateTime())){
                        epicParent.setEndDateTime(subtask.getEndDateTime());
                    }
                }

            }
            case 4 -> {
                System.out.println("Start time: [YYYY-MM-DD HH-MM]");
                task.setStartDateTime(Utils.getInputTime());
                task.setStartDateTime(Utils.getInputTime());
                // recalalculate again
                task.setEndDateTime(Utils.calculateEndTime(task.getStartDateTime(), task.getDuration(), task.getPeriod()));

                if (task instanceof Subtask subtask){
                    Epic epicParent = epics.get(subtask.getParentId());
                    while(subtask.getStartDateTime().isBefore(epicParent.getStartDateTime())){
                        System.out.println("The time must be after the epic's task!");
                        System.out.println("Start time: [YYYY-MM-DD HH-MM]");
                        task.setStartDateTime(Utils.getInputTime());
                        task.setStartDateTime(Utils.getInputTime());
                        task.setEndDateTime(Utils.calculateEndTime(task.getStartDateTime(), task.getDuration(), task.getPeriod()));
                        // rewrite.. the end of the task
                    }
                    if(epicParent.getEndDateTime().isBefore(subtask.getEndDateTime())){
                        epicParent.setEndDateTime(subtask.getEndDateTime());
                    }
                }
            }
            case 5 -> {
                System.out.println("New status:");
                Status status = Utils.getStatus();
                task.setStatus(status);
                if(task instanceof Subtask subtask){
                    updateTask(epics.get(subtask.getParentId()));
                }
            }
        }
    }

    // update epic if its subtasks
    public void updateTask(Epic epic, Long taskId) {
        int input = Utils.updateProperty("Epic");
        switch (input) {
            case 1 -> {
                System.out.println("New title:");
                String title = Utils.getInput();
                epic.setTaskTitle(title);
            }
            case 2 -> {
                System.out.println("New description:");
                String description = Utils.getInput();
                epic.setTaskTitle(description);
            }
        }
    }

    // UPDATE IF SUBTASK HAS BEEN CREATED
    public void updateTask(Epic epic) {

        List<Subtask> subtasks = epic.getSubtaskList();

        boolean allNew = true;
        boolean allDone = true;

        for (Subtask subtask : subtasks) {
            if (subtask.getStatus() != Status.NEW) allNew = false;
            if (subtask.getStatus() != Status.DONE) allDone = false;
        }

        if (allNew) epic.setStatus(Status.NEW);
        else if (allDone) epic.setStatus(Status.DONE);
        else epic.setStatus(Status.IN_PROGRESS);

    }


    // ========================================
    // ============ GETTING TASK =============
    public Task getTaskById(Long taskId) {
//        Managers.getDefaultHistory().add(allTypesTasks.get(taskId)); NOT HERE
        if(epics.containsKey(taskId)) return epics.get(taskId);
        else if(tasks.containsKey(taskId)) return tasks.get(taskId);
        else if(subtasks.containsKey(taskId)) return subtasks.get(taskId);


        // shouldn't run
        return null;
    }

    public List<Task> getAllTasks() {
        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(allTypesTasks.values());
        return allTasks;
    }

    public List<Task> getTaskByPriority(){
        List<Task> taskList = new ArrayList<>(Managers.fileBackendTaskManager.getAllTypesTasks().values());


        return taskList.stream()
                .sorted(Comparator.comparing(Task::getEndDateTime))
                .toList();
    }


    // =================================
    // ======= DELETING ================
    public void deleteAllTasks() {
        allTypesTasks.clear();
        epics.clear();
        subtasks.clear();
        tasks.clear();
    }



    // getEpic / getTask / getSubtask
    public void deleteTaskById(Long taskId) {

        Task task = allTypesTasks.get(taskId);
        if(task == null){
            System.out.println("Task hasn't been found...");
        }

        if(task instanceof Epic epic){
            List<Subtask> subtaskList = getEpicSubtasks(epic);
            for(Subtask subtask : subtaskList){
                Long subId = subtask.getTaskId();
                allTypesTasks.remove(subId);
                subtasks.remove(subId);
            }

            epics.remove(task.getTaskId());
        } else if(task instanceof Subtask subtask){
            // update epic?
            subtasks.remove(subtask.getTaskId());
            updateTask(epics.get(subtask.getParentId()));
        } else {
            tasks.remove(task.getTaskId());
        }

        allTypesTasks.remove(task.getTaskId());

    }


    public List<Subtask> getEpicSubtasks(Epic epic) {
        return epic.getSubtaskList();
    }
    // returns last 10
    // create a list for hasBeenChecked tasks
    public List<Task> history() {
        return Managers.getDefaultHistory().getHistory();
    }



    // ===================================
    // getters/setters
    public HashMap<Long, Epic> getEpics() {
        return epics;
    }

    public HashMap<Long, Subtask> getSubtasks() {
        return subtasks;
    }

    public HashMap<Long, Task> getTasks() {
        return tasks;
    }

    public void setAllTasks(HashMap<Long, Task> allTasks) {
        this.allTypesTasks = allTasks;
    }

    public HashMap<Long, Task> getAllTypesTasks(){
        return allTypesTasks;
    }




}