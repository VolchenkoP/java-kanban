package ru.practicum.tasksManager.exeption;

import java.io.IOException;

public class ManagerSaveException extends IOException {
    public ManagerSaveException(String message) {
        super(message);
    }
}
