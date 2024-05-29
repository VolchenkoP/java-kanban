package ru.practicum.tasksManager.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.practicum.tasksManager.server.handlers.adapters.DurationAdapter;
import ru.practicum.tasksManager.server.handlers.adapters.LocalDateTimeAdapter;
import ru.practicum.tasksManager.service.HistoryManager;
import ru.practicum.tasksManager.service.TaskManager;
import ru.practicum.tasksManager.service.impl.FileBackedTaskManager;
import ru.practicum.tasksManager.service.impl.InMemoryHistoryManager;
import ru.practicum.tasksManager.service.impl.InMemoryTaskManager;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

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
        return FileBackedTaskManager.newFileBackedTaskManager(file);
    }

    public static FileBackedTaskManager getLoadedFileManager(File file) {
        return FileBackedTaskManager.loadTaskManagerFromFile(file);
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        return gsonBuilder.create();
    }

}
