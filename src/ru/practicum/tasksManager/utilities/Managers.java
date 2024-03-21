package ru.practicum.tasksManager.utilities;

import ru.practicum.tasksManager.interfaces.HistoryManager;
import ru.practicum.tasksManager.interfaces.TaskManager;
import ru.practicum.tasksManager.service.InMemoryHistoryManager;
import ru.practicum.tasksManager.service.InMemoryTaskManager;

public class Managers {

    public static TaskManager getDefault(){
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }

}
