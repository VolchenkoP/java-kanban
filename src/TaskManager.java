import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TaskManager {
    private int countId;
    private HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Subtask> subtasks;
    private final HashMap<Integer, Epic> epics;



    public TaskManager() {
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
        countId = 0;
    }

    public void saveTask(Task task) {
        if (!tasks.containsValue(task)) {
            countId++;
            task.setId(countId);
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Такая задача уже создана");
        }
    }

    public void saveSubtask(Subtask subtask) {
        if (!subtasks.containsValue(subtask)) {
            countId++;
            subtask.setId(countId);
            if (subtask.getEpicIdForThisSubtask() != 0){
                if (epics.containsKey(subtask.getEpicIdForThisSubtask())){
                    epics.get(subtask.getEpicIdForThisSubtask()).setSubtasksForThisEpic(subtask);
                }
            }
            subtasks.put(subtask.getId(), subtask);
        } else {
            System.out.println("Такая подзадача уже создана");
        }

    }

    public void saveEpic(Epic epic) {
        if (!epics.containsValue(epic)) {
            countId++;
            epic.setId(countId);
            epics.put(epic.getId(), epic);
        } else {
            System.out.println("Такой эпик уже создан");
        }
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public void deleteById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        }
        if (epics.containsKey(id)) {
            for (Integer num : subtasks.keySet()){
                if (subtasks.get(num).getEpicIdForThisSubtask() == id){
                    subtasks.get(num).deleteEpicId();
                }
            }
            epics.remove(id);
        }
        if (subtasks.containsKey(id)) {
            if (subtasks.get(id).getEpicIdForThisSubtask() != 0){
                epics.get(subtasks.get(id).getEpicIdForThisSubtask()).deleteSubtaskForThisEpic(id);
                changeEpicStatus(epics.get(subtasks.get(id).getEpicIdForThisSubtask()));
            }
            subtasks.remove(id);
        }
    }

    public void updateTask(Task task) {
        tasks.get(task.getId()).setName(task.getName());
        tasks.get(task.getId()).setDescription(task.getDescription());
        tasks.get(task.getId()).setStatus(task.getStatus());
    }

    public void updateSubtask(Subtask subtask) {
        subtasks.get(subtask.getId()).setName(subtask.getName());
        subtasks.get(subtask.getId()).setDescription(subtask.getDescription());
        subtasks.get(subtask.getId()).setStatus(subtask.getStatus());
        changeEpicStatus(epics.get(subtask.getEpicIdForThisSubtask()));
    }

    private void changeEpicStatus(Epic epic) {
        if (epic.getSubtasksForThisEpic() == null){
            epic.setStatus(Status.NEW);
        } else {
            int countNew = 0;
            int countProgress = 0;
            int countDone = 0;
            HashMap <Integer, Subtask> forStatus = epic.getSubtasksForThisEpic();
            for (Integer num : forStatus.keySet()) {
                if (forStatus.get(num).getStatus() == Status.NEW) {
                    countNew++;
                } else if (forStatus.get(num).getStatus() == Status.IN_PROGRESS){
                    countProgress++;
                } else if (forStatus.get(num).getStatus() == Status.DONE) {
                    countDone++;
                }
            }
            if (countProgress != 0){
                epic.setStatus(Status.IN_PROGRESS);
            }
            if ((countNew == 0) && (countProgress == 0) && (countDone > 0)){
                epic.setStatus(Status.DONE);
            }
        }
    }

    public void connectEpicAndSubtask(Subtask subtask, Epic epic){
        subtask.setEpicIdForThisSubtask(epic.getId());
        epic.setSubtasksForThisEpic(subtask);
    }

    public HashMap<Epic, HashMap<Integer, Subtask>> getAllSubtasksByEpic (Epic epic){
        HashMap<Epic, HashMap<Integer, Subtask>> subtasksByEpic= new HashMap<>();
        subtasksByEpic.put(epic, epic.getSubtasksForThisEpic());
        return subtasksByEpic;
    }

    public void printTasks(){
        System.out.println(tasks);
    }
    public void printSubtasks(){
        System.out.println(subtasks);
    }

    public void printEpics(){
        System.out.println(epics);
    }

}
