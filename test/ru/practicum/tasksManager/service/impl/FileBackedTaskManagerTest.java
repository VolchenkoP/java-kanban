package ru.practicum.tasksManager.service.impl;

import org.junit.jupiter.api.Test;
import ru.practicum.tasksManager.exception.ManagerSaveException;
import ru.practicum.tasksManager.model.Epic;
import ru.practicum.tasksManager.model.Status;
import ru.practicum.tasksManager.model.Subtask;
import ru.practicum.tasksManager.model.Task;
import ru.practicum.tasksManager.utilities.Managers;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    private final File fileSavedHistory;

    {
        try {
            fileSavedHistory = File.createTempFile("data", ".txt");
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка создания файла в тесте FileBackManager");
        }
    }

    @Override
    public FileBackedTaskManager createTaskManager() {
        return taskManager = new FileBackedTaskManager(fileSavedHistory);
    }

    @Test
    public void saveTaskShouldSuccessfullySaveTaskGetTaskByIdGetIdTest() {
        final Task expectedTask = new Task("Задача 1", "После спринта8 сдать спринт9", Status.NEW);
        expectedTask.setStartTime(LocalDateTime.now());
        expectedTask.setDuration(Duration.ofMinutes(15));

        taskManager.saveTask(expectedTask);

        final int idTask = taskManager.getTaskById(expectedTask.getId()).getId();

        assertEquals(expectedTask.getId(), idTask, "Задачи не равны");
    }

    @Test
    public void saveEpicShouldSuccessfullySaveEpicGetEpicByIdGetIdTest() {
        final Epic expectedEpic = new Epic("Эпик 1", "Пройти обучение Java");
        taskManager.saveEpic(expectedEpic);

        final int idEpic = taskManager.getEpicById(expectedEpic.getId()).getId();

        assertEquals(expectedEpic.getId(), idEpic, "Эпики не равны");
    }

    @Test
    public void saveSubtaskShouldSuccessfullySaveSubtaskGetSubtaskByIdGetIdTest() {
        final Epic expectedEpic = new Epic("Эпик 1", "Пройти обучение Java");
        taskManager.saveEpic(expectedEpic);
        final Subtask expectedSubtask = new Subtask("Подзадача 1", "Пройти практику Java");
        expectedSubtask.setEpicIdForThisSubtask(expectedEpic.getId());
        expectedSubtask.setStartTime(LocalDateTime.now());
        expectedSubtask.setDuration(Duration.ofMinutes(15));
        taskManager.saveSubtask(expectedSubtask);

        final int idSubTask = taskManager.getSubtaskById(expectedSubtask.getId()).getId();

        assertEquals(expectedSubtask.getId(), idSubTask, "Подзадачи не равны");
    }

    @Test
    public void getLoadedFileManagerShouldSuccessfullyGetLoadedFileManagerFromEmptyFileGetTasksTest() {
        final FileBackedTaskManager newBackedTasksManagerEmpty = Managers.getLoadedFileManager(fileSavedHistory);
        final List<Task> emptyListTasks = newBackedTasksManagerEmpty.getTasks();
        assertEquals(0, emptyListTasks.size(), "Список задач не пуст");
    }

    @Test
    public void getLoadedFileManagerShouldSuccessfullySaveTaskEpicSubtaskGetLoadedFileManagerFromNotEmptyFileTest() {
        final Task task = new Task("Task1", "des1", Status.NEW);
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofMinutes(15));
        taskManager.saveTask(task);
        taskManager.getTaskById(task.getId());

        final Epic epic = new Epic("Epic1", "Des1");
        taskManager.saveEpic(epic);

        final Subtask subtask = new Subtask("Sub1", "des1");
        subtask.setStartTime(LocalDateTime.now().plusMinutes(20));
        subtask.setDuration(Duration.ofMinutes(15));
        subtask.setEpicIdForThisSubtask(epic.getId());
        taskManager.saveSubtask(subtask);

        final FileBackedTaskManager newBackedTasksManagerEmpty = Managers.getLoadedFileManager(taskManager.getFileToHistory());
        final List<Task> notEmptyListTasks = newBackedTasksManagerEmpty.getTasks();
        final List<Epic> notEmptyListEpics = newBackedTasksManagerEmpty.getEpics();
        final List<Subtask> notEmptyListSubtasks = newBackedTasksManagerEmpty.getSubtasks();
        final Status taskStatus = task.getStatus();
        final Status taskAfterLoadedStatus = notEmptyListTasks.getFirst().getStatus();
        final Status epicStatus = epic.getStatus();
        final LocalDateTime startTimeSubtask = subtask.getStartTime();
        final Status epicAfterLoadStatus = notEmptyListEpics.getFirst().getStatus();
        final LocalDateTime startTimeSubtaskAfterLoad = notEmptyListSubtasks.getFirst().getStartTime();
        assertEquals(1, notEmptyListTasks.size(), "Список задач не пуст");
        assertEquals(taskStatus, taskAfterLoadedStatus, "Задачи не совпадают по статусу");
        assertEquals(epicStatus, epicAfterLoadStatus, "Эпики не совпадают по статусу");
        assertEquals(startTimeSubtask, startTimeSubtaskAfterLoad, "Подзадачи не совпадают повремени");
    }

}