package ru.practicum.tasksManager.service.impl;

import org.junit.jupiter.api.Test;
import ru.practicum.tasksManager.exeption.ManagerSaveException;
import ru.practicum.tasksManager.model.Epic;
import ru.practicum.tasksManager.model.Status;
import ru.practicum.tasksManager.model.Subtask;
import ru.practicum.tasksManager.model.Task;
import ru.practicum.tasksManager.utilities.Managers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FileBackedTaskManagerTest {

    File file;
    {
        try {
            file = File.createTempFile("data", ".txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void addNewTask() {
        FileBackedTaskManager fileBackedTaskManager = Managers.getDefaultFileManager(file);
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        fileBackedTaskManager.saveTask(task);

        final int taskId = task.getId();

        final Task savedTask = fileBackedTaskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = fileBackedTaskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.getFirst(), "Задачи не совпадают.");

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileBackedTaskManager.getFileToHistory()));
            writer.write("");
            writer.close();
        } catch (ManagerSaveException e) {
            e.getMessage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void addNewEpic() {
        FileBackedTaskManager fileBackedTaskManager = Managers.getDefaultFileManager(file);

        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        fileBackedTaskManager.saveEpic(epic);

        final int epicId = epic.getId();

        final Epic savedEpic = fileBackedTaskManager.getEpicById(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");

        final List<Epic> epics = fileBackedTaskManager.getEpics();

        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.getFirst(), "Задачи не совпадают.");

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileBackedTaskManager.getFileToHistory()));
            writer.write("");
            writer.close();
        } catch (ManagerSaveException e) {
            e.getMessage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void deleteSubtask() {
        FileBackedTaskManager fileBackedTaskManager = Managers.getDefaultFileManager(file);
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        Subtask subtask = new Subtask("Subtask1", "Desk1");

        fileBackedTaskManager.saveEpic(epic);
        subtask.setEpicIdForThisSubtask(epic.getId());

        fileBackedTaskManager.saveSubtask(subtask);

        fileBackedTaskManager.deleteById(epic.getId());
        final int checkSubtasksListSize = fileBackedTaskManager.getSubtasks().size();

        assertEquals(0, checkSubtasksListSize, "После удаления эпика подзадача "
                + "не удалилась");

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileBackedTaskManager.getFileToHistory()));
            writer.write("");
            writer.close();
        } catch (ManagerSaveException e) {
            e.getMessage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createAndLoadTasksFromFile() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        FileBackedTaskManager fileBackedTaskManager = Managers.getDefaultFileManager(file);
        fileBackedTaskManager.saveEpic(epic);
        final List<Epic> forTest = fileBackedTaskManager.getEpics();
        FileBackedTaskManager fileBackedTaskManagerAnother = Managers.getDefaultFileManager(file);
        final List<Epic> forTestAnother = fileBackedTaskManagerAnother.getEpics();
        assertEquals(forTest, forTestAnother, "Списки не совпадают.");

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileBackedTaskManager.getFileToHistory()));
            writer.write("");
            writer.close();
        } catch (ManagerSaveException e) {
            e.getMessage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}