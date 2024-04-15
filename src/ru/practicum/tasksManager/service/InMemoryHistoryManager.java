package ru.practicum.tasksManager.service;

import ru.practicum.tasksManager.interfaces.HistoryManager;
import ru.practicum.tasksManager.model.Task;

import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManager {
    private final LinkedList<Task> requestHistory = new LinkedList<>();

    @Override
    public void add(Task task) {
        Task taskForHistory = (Task) task.clone();
        taskForHistory.setId(task.getId());
        if (requestHistory.size() == 10) {
            requestHistory.removeFirst();
            requestHistory.add(taskForHistory);
        } else {
            requestHistory.add(taskForHistory);
        }
    }

    @Override
    public LinkedList<Task> getHistory() {
        return requestHistory;
    }
}
