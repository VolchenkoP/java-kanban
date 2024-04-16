package ru.practicum.tasksManager.utilities;

import ru.practicum.tasksManager.service.HistoryManager;
import ru.practicum.tasksManager.service.TaskManager;
import ru.practicum.tasksManager.service.impl.InMemoryHistoryManager;
import ru.practicum.tasksManager.service.impl.InMemoryTaskManager;

public class Managers {

    private Managers() {
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
