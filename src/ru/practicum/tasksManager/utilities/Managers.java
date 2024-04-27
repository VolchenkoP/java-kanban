package ru.practicum.tasksManager.utilities;

import ru.practicum.tasksManager.service.HistoryManager;
import ru.practicum.tasksManager.service.TaskManager;
import ru.practicum.tasksManager.service.impl.FileBackedTaskManager;
import ru.practicum.tasksManager.service.impl.InMemoryHistoryManager;
import ru.practicum.tasksManager.service.impl.InMemoryTaskManager;

import java.io.File;

public class Managers {

    private Managers() {
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTaskManager getDefaultFileManager(File file) {
        return new FileBackedTaskManager(file);
    }

}
