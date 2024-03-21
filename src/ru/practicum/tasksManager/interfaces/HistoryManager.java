package ru.practicum.tasksManager.interfaces;

import ru.practicum.tasksManager.model.Task;

import java.util.LinkedList;

public interface HistoryManager {

    void add(Task task);

    LinkedList<Task> getHistory();
}
