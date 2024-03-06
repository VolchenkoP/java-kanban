import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    static int count;
    private static HashMap<Integer, Task> tasks;
    private static HashMap<Integer, Subtask> subtasks;
    private static HashMap<Integer, Epic> epics;

    private static HashMap<Epic, ArrayList<Subtask>> subtasksByEpic;

    public TaskManager() {
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
        subtasksByEpic = new HashMap<>();
        count = 0;
    }

    public static void saveTask(Task task) {
        if (!tasks.containsValue(task)) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Такая задача уже создана");
        }
    }

    public static void saveSubtask(Subtask subtask) {
        if (!subtasks.containsValue(subtask)) {
            subtasks.put(subtask.getId(), subtask);
        } else {
            System.out.println("Такая подзадача уже создана");
        }
    }

    public static void saveEpic(Epic epic) {
        if (!epics.containsValue(epic)) {
            epics.put(epic.getId(), epic);
        } else {
            System.out.println("Такой эпик уже создан");
        }
    }


    public static Object getTasksByType(String typeOfTasks) {
        HashMap<Integer, Object> type = new HashMap<>();
        switch (typeOfTasks) {
            case "Задачи" -> {
                for (Integer key : tasks.keySet()) {
                    type.put(key, tasks.get(key));
                }
            }
            case "Эпики" -> {
                for (Integer key : epics.keySet()) {
                    type.put(key, epics.get(key));
                }
            }
            case "Подзадачи" -> {
                for (Integer key : subtasks.keySet()) {
                    type.put(key, subtasks.get(key));
                }
            }
        }
        return type.values();
    }

    public static void deleteAllTaskByType(String typeOfTasks) {
        switch (typeOfTasks) {
            case "Задачи" -> tasks.clear();
            case "Эпики" -> {
                epics.clear();
                subtasksByEpic.clear();
            }
            case "Подзадачи" -> {
                subtasks.clear();
                subtasksByEpic.clear();
            }
        }
    }

    public static Task getTaskById(int id) {
        return tasks.get(id);
    }

    public static Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public static Epic getEpicById(int id) {
        return epics.get(id);
    }

    public static void deleteById(int id) {
        while (true) {
            for (Integer val : tasks.keySet()) {
                if (val == id) {
                    tasks.remove(id);
                    return;
                }
            }
            for (Integer val : epics.keySet()) {
                if (val == id) {
                    subtasksByEpic.remove(epics.get(id));
                    epics.remove(id);
                    return;
                }
            }
            for (Integer val : subtasks.keySet()) {
                if (val == id) {
                    for (Epic epic : subtasksByEpic.keySet()) {
                        if (subtasksByEpic.get(epic).contains(subtasks.get(id))) {
                            subtasksByEpic.get(epic).remove(subtasks.get(id));
                            changeEpicStatus(epic);
                        }
                    }
                    subtasks.remove(id);
                    return;
                }
            }
        }
    }

    public static void updateTask(Task task, String name, String description, Status status) {
        task.setName(name);
        task.setDescription(description);
        task.setStatus(status);
    }

    public static void updateSubtask(Subtask subtask, String name, String description, Status status) {
        subtask.setName(name);
        subtask.setDescription(description);
        subtask.setStatus(status);
        for (Epic epic : subtasksByEpic.keySet()) {
            ArrayList<Subtask> check = subtasksByEpic.get(epic);
            if (check.contains(subtask)) {
                changeEpicStatus(epic);
            }
        }
    }

    public static Task createTask(String name, String description, Status status) {
        return new Task(name, description, status);
    }

    public static Subtask createSubtask(String name, String description, Status status) {
        return new Subtask(name, description, status);
    }

    public static Epic createEpic(String name, String description, Status status) {
        return new Epic(name, description, status);
    }


    public static void setSubtasksByEpic(Epic epic, Subtask subtask) {
        if (subtasksByEpic.containsKey(epic)) {
            subtasksByEpic.get(epic).add(subtask);
        } else {
            subtasksByEpic.computeIfAbsent(epic, k -> new ArrayList<>()).add(subtask);
        }
    }

    private static void changeEpicStatus(Epic epic) {
        ArrayList<Subtask> forStatus = subtasksByEpic.get(epic);
        int count = 0;
        for (Subtask subtask : forStatus) {
            if (subtask.getStatus().equals(Status.IN_PROGRESS)) {
                epic.setStatus(Status.IN_PROGRESS);
            } else if (subtask.getStatus().equals(Status.DONE)) {
                count++;
            }
        }
        if (count == forStatus.size()) {
            epic.setStatus(Status.DONE);
        }
    }

}
