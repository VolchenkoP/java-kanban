package ru.practicum.tasksManager.service.impl;

import org.junit.jupiter.api.Test;
import ru.practicum.tasksManager.exception.ManagerSaveException;
import ru.practicum.tasksManager.model.Epic;
import ru.practicum.tasksManager.model.Status;
import ru.practicum.tasksManager.model.Subtask;
import ru.practicum.tasksManager.model.Task;
import ru.practicum.tasksManager.utilities.Managers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    private final File file;
    FileBackedTaskManager fileBackedTaskManager;

    {
        try {
            file = File.createTempFile("data", ".txt");
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка создания файла в тесте FileBackedTaskManagerTest");
        }
    }

    @Test
    void addNewTask() throws ManagerSaveException {
        fileBackedTaskManager = Managers.getDefaultFileManager(file);
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofMinutes(15));
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
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи для очистки файла при тесте задачи");
        }
    }

    @Test
    void shouldBeCorrectLocalDateTimeSaveEpicToFile() {
        fileBackedTaskManager = Managers.getDefaultFileManager(file);

        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        epic.setStartTime(LocalDateTime.now());
        epic.setDuration(Duration.ofMinutes(15));
        fileBackedTaskManager.saveEpic(epic);

        final int epicId = epic.getId();
        final Epic savedEpic = fileBackedTaskManager.getEpicById(epicId);
        final LocalDateTime dateTimeEpicBeforeLoad = epic.getStartTime();

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");

        final List<Epic> epics = fileBackedTaskManager.getEpics();

        FileBackedTaskManager anotherFileBackManager = Managers.getLoadedFileManager(file);
        final Epic epicFromFile = anotherFileBackManager.getEpics().get(0);
        final LocalDateTime dateTimeEpicAfterLoad = epicFromFile.getStartTime();
        final List<Epic> epicsAfterLoad = anotherFileBackManager.getEpics();


        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic, epics.getFirst(), "Эпики не совпадают.");
        assertEquals(epics, epicsAfterLoad, "Эпики не совпадают");
        assertEquals(dateTimeEpicBeforeLoad, dateTimeEpicAfterLoad, "Время старта эпика не сопадает");

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileBackedTaskManager.getFileToHistory()));
            writer.write("");
            writer.close();
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи для очистки файла при тесте эпика");
        }
    }

    @Test
    void deleteSubtask() {
        fileBackedTaskManager = Managers.getDefaultFileManager(file);
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        Subtask subtask = new Subtask("Subtask1", "Desk1");
        subtask.setStartTime(LocalDateTime.now());
        subtask.setDuration(Duration.ofMinutes(15));

        fileBackedTaskManager.saveEpic(epic);
        subtask.setEpicIdForThisSubtask(epic.getId());

        fileBackedTaskManager.saveSubtask(subtask);

        fileBackedTaskManager.deleteEpicById(epic.getId());
        final int checkSubtasksListSize = fileBackedTaskManager.getSubtasks().size();

        assertEquals(0, checkSubtasksListSize, "После удаления эпика подзадача "
                + "не удалилась");

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileBackedTaskManager.getFileToHistory()));
            writer.write("");
            writer.close();
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи для отчистки файла при тесте удаления подзадачи");
        }
    }

    @Test
    void createAndLoadTasksFromFile() {
        Task task = new Task("Test addNewEpic", "Test addNewEpic description", Status.NEW);
        fileBackedTaskManager = Managers.getDefaultFileManager(file);
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofMinutes(15));
        fileBackedTaskManager.saveTask(task);

        final LocalDateTime startTimeBeforeSavingToFile = task.getStartTime();
        final List<Task> forTest = fileBackedTaskManager.getTasks();

        FileBackedTaskManager fileBackedTaskManagerAnother = Managers.getLoadedFileManager(file);

        final List<Task> forTestAnother = fileBackedTaskManagerAnother.getTasks();
        final LocalDateTime startTimeAfterSavingToFile = forTestAnother.getFirst().getStartTime();

        assertEquals(forTest, forTestAnother, "Списки не совпадают.");
        assertEquals(startTimeBeforeSavingToFile, startTimeAfterSavingToFile, "Даты задач не совпадают");

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileBackedTaskManager.getFileToHistory()));
            writer.write("");
            writer.close();
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи для отчистки файла при тесте записи и загурзкис файлом");
        }
    }

}