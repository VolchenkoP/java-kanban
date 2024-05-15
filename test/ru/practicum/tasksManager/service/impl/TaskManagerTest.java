package ru.practicum.tasksManager.service.impl;

import org.junit.jupiter.api.Test;
import ru.practicum.tasksManager.model.Epic;
import ru.practicum.tasksManager.model.Status;
import ru.practicum.tasksManager.model.Subtask;
import ru.practicum.tasksManager.model.Task;
import ru.practicum.tasksManager.service.HistoryManager;
import ru.practicum.tasksManager.service.TaskManager;
import ru.practicum.tasksManager.utilities.Managers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    TaskManager taskManager;
    HistoryManager historyManager;

    @Test
    void shouldReturnHistoryWithTask() {
        historyManager = Managers.getDefaultHistory();
        final Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        historyManager.addToHistory(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
        assertTrue(task.equals(history.get(0)), "Задачи не совпадают");
    }

    @Test
    void shouldReturnListWithTask() {
        taskManager = Managers.getDefault();
        final Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        final Task task2 = new Task("Test addNewTask2", "Test addNewTask2 description", Status.NEW);
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofMinutes(15));
        task2.setStartTime(LocalDateTime.now().plusMinutes(16));
        task2.setDuration(Duration.ofMinutes(15));
        taskManager.saveTask(task);
        taskManager.saveTask(task2);
        final List<Task> listOfTasks = taskManager.getTasks();
        assertNotNull(listOfTasks, "Лист пустой.");
        assertEquals(2, listOfTasks.size(), "Количество задач не совпадает.");
        assertTrue(task.equals(listOfTasks.get(0)), "Задачи не совпадают");
    }

    @Test
    void shouldListOfTasksHaveZeroAfterDeleteThem() {
        taskManager = Managers.getDefault();
        final Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        final Task task2 = new Task("Test addNewTask2", "Test addNewTask2 description", Status.NEW);
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofMinutes(15));
        task2.setStartTime(LocalDateTime.now().plusMinutes(16));
        task2.setDuration(Duration.ofMinutes(15));
        taskManager.saveTask(task);
        taskManager.saveTask(task2);
        final List<Task> listOfTasks = taskManager.getTasks();
        assertNotNull(listOfTasks, "Лист пустой.");
        assertEquals(2, listOfTasks.size(), "Количество задач не совпадает.");
        assertTrue(task.equals(listOfTasks.get(0)), "Задачи не совпадают");
    }

    @Test
    void shouldBeTaskCorrectSave() {
        taskManager = Managers.getDefault();
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        taskManager.saveTask(task);

        final int taskId = task.getId();

        final Task savedTask = taskManager.getTaskById(taskId).get();

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.getFirst(), "Задачи не совпадают.");
    }

    @Test
    void shouldBeEpicCorrectSave() {
        taskManager = Managers.getDefault();
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        taskManager.saveEpic(epic);

        final int epicId = epic.getId();

        final Epic savedEpic = taskManager.getEpicById(epicId).get();

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");

        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.getFirst(), "Задачи не совпадают.");
    }

    @Test
    void shouldBeSubtaskCorrectDeleteAfterDeleteEpic() {
        taskManager = Managers.getDefault();
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        Subtask subtask = new Subtask("Subtask1", "Desk1");
        subtask.setStartTime(LocalDateTime.now());
        subtask.setDuration(Duration.ofMinutes(15));

        taskManager.saveEpic(epic);
        subtask.setEpicIdForThisSubtask(epic.getId());

        taskManager.saveSubtask(subtask);

        taskManager.deleteById(epic.getId());
        final int checkSubtasksListSize = taskManager.getSubtasks().size();

        assertEquals(0, checkSubtasksListSize, "После удаления эпика подзадача "
                + "не удалилась");
    }

}
