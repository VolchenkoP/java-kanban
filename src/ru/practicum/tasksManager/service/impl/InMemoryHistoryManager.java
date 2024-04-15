package ru.practicum.tasksManager.service.impl;

import ru.practicum.tasksManager.model.Task;
import ru.practicum.tasksManager.service.HistoryManager;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int SIZE_OF_REQUEST_HISTORY = 10;
    private final List<Task> requestHistory = new LinkedList<>();

    @Override
    public void addToHistory(Task task) {
        if (task != null) {
            if (requestHistory.size() == SIZE_OF_REQUEST_HISTORY) {
                requestHistory.removeFirst();
            }
            requestHistory.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return List.copyOf(requestHistory);
    }
}
