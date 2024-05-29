package ru.practicum.tasksManager.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {

    private int epicIdForThisSubtask;

    public Subtask(String name, String description) {
        super(name, description, Status.NEW);
        epicIdForThisSubtask = 0;
    }

    public Subtask(int id, String name, String description, Status status, LocalDateTime startTime,
                   Duration duration, int idEpic) {
        super(name, description, status);
        this.setId(id);
        this.setStartTime(startTime);
        this.setDuration(duration);
        epicIdForThisSubtask = idEpic;
    }

    public Subtask(String name, String description, int duration, LocalDateTime startTime, int idEpic) {
        super(name, description, Status.NEW);
        this.setDuration(Duration.ofMinutes(duration));
        this.setStartTime(startTime);
        epicIdForThisSubtask = idEpic;
    }

    public int getEpicIdForThisSubtask() {
        return epicIdForThisSubtask;
    }

    public void setEpicIdForThisSubtask(int id) {
        if (epicIdForThisSubtask == 0) {
            epicIdForThisSubtask = id;
        } else {
            System.out.println("У данной подзадачи уже есть Эпик");
        }
    }

}
