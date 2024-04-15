package ru.practicum.tasksManager.model;

import java.util.HashMap;

public class Epic extends Task {
    private final HashMap<Integer, Subtask> subtasksForThisEpic;

    public Epic(String name, String description) {
        super(name, description, Status.NEW);

        subtasksForThisEpic = new HashMap<>();
    }

    public void addSubtasksForThisEpic(Subtask subtask) {
        if (!subtasksForThisEpic.containsKey(subtask.getId())) {
            subtasksForThisEpic.put(subtask.getId(), subtask);
        } else {
            System.out.println("Данная подзадача уже сохранена в Эпике");
        }
    }

    public void deleteSubtaskForThisEpic(int id) {
        if (subtasksForThisEpic.containsKey(id)) {
            subtasksForThisEpic.remove(id);
        } else {
            System.out.println("У этого Эпика нет такой подзадачи");
        }
    }

    public HashMap<Integer, Subtask> getSubtasksForThisEpic() {
        return subtasksForThisEpic;
    }

}
