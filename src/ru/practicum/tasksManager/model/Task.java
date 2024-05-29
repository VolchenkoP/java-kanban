package ru.practicum.tasksManager.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task implements Cloneable {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd.MM.yy");
    private String name;
    private String description;
    private int id;
    private Status status;
    private TypeOfTask type;
    private Duration duration;
    private LocalDateTime startTime;

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(String name, String description, Status status, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TypeOfTask getTypeOfTask() {
        return type;
    }

    public void setTypeOfTask(TypeOfTask typeOfTask) {
        this.type = typeOfTask;
    }

    public LocalDateTime getEndTime() {
        return startTime == null ? null : startTime.plusMinutes(duration.toMinutes());
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        String formattedLocalDateTime = startTime.format(DATE_TIME_FORMATTER);
        this.startTime = LocalDateTime.parse(formattedLocalDateTime, DATE_TIME_FORMATTER);
    }

    @Override
    public String toString() {
        String toString = Integer.toString(getId()) +
                ',' + getTypeOfTask() +
                "," + name +
                "," + status +
                "," + description;
        if (duration.toMinutes() == 0) {
            return toString + "," + duration + "," + null;
        } else {
            return toString + "," + duration + "," + startTime;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

}
