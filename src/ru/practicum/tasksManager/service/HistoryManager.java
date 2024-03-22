package ru.practicum.tasksManager.service;

import ru.practicum.tasksManager.model.Task;

import java.util.List;

public interface HistoryManager {

    void addToHistory(Task task);

    List<Task> getHistory();
}
