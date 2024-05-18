package ru.practicum.tasksManager.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.tasksManager.model.Epic;
import ru.practicum.tasksManager.model.Status;
import ru.practicum.tasksManager.model.Subtask;
import ru.practicum.tasksManager.model.Task;
import ru.practicum.tasksManager.service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    @BeforeEach
    public void init() {
        createTaskManager();
    }

    protected abstract T createTaskManager();

    //1
    @Test
    public void saveTaskShouldSuccessfullySaveTaskTest() {
        final Task task = new Task("Задача 1", "Сдать спринт4", Status.NEW);
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofMinutes(15));
        taskManager.saveTask(task);

        final List<Task> taskList = taskManager.getTasks();
        final Task added1Task = taskManager.getTaskById(task.getId());

        assertNotNull(taskList, "Задача не сохранилась");
        assertEquals(taskList.getFirst(), task, "Задачи не совпадают");
        assertEquals(1, taskList.size(), "Неверное количество задач");
        assertEquals(added1Task, taskList.getFirst(), "Задача не совпадают");
    }

    @Test
    public void setStartTimeAndDurationToTaskShouldSuccessfullySetStartTimeAndDurationTest() {
        final Task task = new Task("Задача 7", "После спринта8 сдать спринт9", Status.NEW);
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofMinutes(15));
        taskManager.saveTask(task);
        final LocalDateTime timeStartIsNotEmpty = task.getStartTime();
        final LocalDateTime timeEndIsNotEmpty = task.getEndTime();
        final long durationIsNotEmpty = task.getDuration().toMinutes();

        assertNotNull(timeStartIsNotEmpty, "Время старта Null");
        assertNotNull(timeEndIsNotEmpty, "Время конца Null");
        assertEquals(15, durationIsNotEmpty, "Продолжительность не 15");

        task.setStartTime(LocalDateTime.parse("2024-05-18T09:00"));
        task.setDuration(Duration.ofMinutes(15));

        final LocalDateTime timeStartNotEmpty = LocalDateTime.parse("2024-05-18T09:00");
        final long durationNotEmpty = 15;
        final LocalDateTime timeEndNotEmpty = timeStartNotEmpty.plusMinutes(durationNotEmpty);

        final LocalDateTime timeStartTask = task.getStartTime();
        final LocalDateTime timeEndTask = task.getEndTime();
        final long durationTask = task.getDuration().toMinutes();

        assertEquals(timeStartTask, timeStartNotEmpty, "Время старта не совпадает");
        assertEquals(timeEndTask, timeEndNotEmpty, "Время конца не совпадает");
        assertEquals(durationTask, durationNotEmpty, "Продолжительность не совпадает");
    }

    @Test
    public void saveEpicShouldSuccessfullySaveEpicTest() {
        final Epic epic1 = new Epic("Эпик 1", "Пройти обучение Java");
        epic1.setStartTime(LocalDateTime.now());
        epic1.setDuration(Duration.ofMinutes(15));
        taskManager.saveEpic(epic1);

        final Epic added1Epic = taskManager.getEpicById(epic1.getId());
        final List<Epic> epicList = taskManager.getEpics();

        assertNotNull(epicList, "Эпик null");
        assertEquals(1, epicList.size(), "Неверное количество эпиков");
        assertEquals(epic1, epicList.getFirst(), "Эпики не совпадают");
        assertNotNull(added1Epic, "Эпик не найден");
    }

    //2
    @Test
    public void saveSubtaskShouldSuccessfullySaveSubtaskTest() {
        final Epic epic2 = new Epic("Эпик 2", "Трудойстройтсво на Java разработчика");
        taskManager.saveEpic(epic2);
        final Epic added2Epic = taskManager.getEpicById(epic2.getId());
        assertNotNull(added2Epic, "Эпик null");

        final Subtask subtask1 = new Subtask("Подзадача 1", "Пройти теорию Java");
        subtask1.setEpicIdForThisSubtask(epic2.getId());
        subtask1.setStatus(Status.NEW);
        subtask1.setStartTime(LocalDateTime.now());
        subtask1.setDuration(Duration.ofMinutes(15));
        taskManager.saveSubtask(subtask1);

        final Subtask added1subtask = taskManager.getSubtaskById(subtask1.getId());
        final List<Subtask> subtaskList = taskManager.getAllSubtasksByEpic(epic2.getId());

        assertNotNull(added1subtask, "Подзадача не найдена");
        assertNotNull(subtaskList, "Подзадача Null");
        assertEquals(1, subtaskList.size(), "Неверное количество Подзадач");
        assertEquals(subtask1, subtaskList.getFirst(), "Подзадачи не совпадают");
    }

    @Test
    public void updateTaskShouldSuccessfullyUpdateTaskStatusTest() {
        final Task task1 = new Task("Задача 1", "Сдать спринт4", Status.NEW);
        task1.setStartTime(LocalDateTime.now());
        task1.setDuration(Duration.ofMinutes(15));
        taskManager.saveTask(task1);

        task1.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task1);
        final Status tasksStatusAfterUpdate = taskManager.getTaskById(task1.getId()).getStatus();

        assertEquals(Status.IN_PROGRESS, tasksStatusAfterUpdate, "Статус не обновился");
    }

    @Test
    public void updateEpicShouldSuccessfullyUpdateEpicDescriptionTest() {
        final Epic epic = new Epic("E1", "Desc1");
        taskManager.saveEpic(epic);
        epic.setDescription("UpdateDesc1");
        taskManager.updateEpic(epic);
        final String expectedDescription = "UpdateDesc1";
        final String epicDescription = taskManager.getEpicById(epic.getId()).getDescription();
        assertEquals(expectedDescription, epicDescription, "Эпик не обновился");
    }

    @Test
    public void updateSubtaskShouldSuccessfullyUpdateSubtaskStatusTest() {
        final Epic epic = new Epic("E1", "Сдать");
        taskManager.saveEpic(epic);

        final Subtask subtask = new Subtask("S1", "des1");
        subtask.setStartTime(LocalDateTime.now());
        subtask.setDuration(Duration.ofMinutes(15));
        subtask.setEpicIdForThisSubtask(epic.getId());
        taskManager.saveSubtask(subtask);

        final Status subtaskStatusBeforeUpdate = subtask.getStatus();
        subtask.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask);
        final Status subtaskStatusAfterUpdateToProgress = taskManager.getSubtaskById(subtask.getId()).getStatus();

        assertNotEquals(subtaskStatusBeforeUpdate, subtaskStatusAfterUpdateToProgress, "Статус не обновился");
        assertEquals(Status.IN_PROGRESS, subtaskStatusAfterUpdateToProgress, "Статус не обновился");
    }

    @Test
    public void setStartTimeAndDurationToSubtaskShouldSuccessfullySetStartTimeAndDurationToSubtaskTest() {
        final Epic epic1 = new Epic("Эпик 1", "Пройти обучение Java");
        taskManager.saveEpic(epic1);
        final Subtask subtask1 = new Subtask("Sub1", "des1");
        subtask1.setEpicIdForThisSubtask(epic1.getId());
        subtask1.setStartTime(LocalDateTime.now());
        subtask1.setDuration(Duration.ofMinutes(15));
        taskManager.saveSubtask(subtask1);

        final Subtask added1Subtask = taskManager.getSubtaskById(subtask1.getId());
        final LocalDateTime timeStartNotEmpty = LocalDateTime.parse("2024-05-18T09:00");
        final long durationNotEmpty = 15;
        final LocalDateTime timeEndNotEmpty = timeStartNotEmpty.plusMinutes(durationNotEmpty);

        added1Subtask.setStartTime(timeStartNotEmpty);
        added1Subtask.setDuration(Duration.ofMinutes(durationNotEmpty));

        final LocalDateTime timeStartSubtask = added1Subtask.getStartTime();
        final LocalDateTime timeEndSubtask = added1Subtask.getEndTime();
        final long durationTask = added1Subtask.getDuration().toMinutes();

        assertEquals(timeStartSubtask, timeStartNotEmpty, "Время старта не совпадает");
        assertEquals(timeEndSubtask, timeEndNotEmpty, "Время конца не совпадает");
        assertEquals(durationTask, durationNotEmpty, "Продолжительность не совпадает");
    }

    @Test
    public void getTasksShouldSuccessfullySaveTasksGetTasksTest() {
        final Task added3Task = new Task("Задача 1", "Сдать спринт8", Status.NEW);
        final Task added4Task = new Task("Задача 2", "После спринта9 сдать спринт7", Status.IN_PROGRESS);
        added3Task.setStartTime(LocalDateTime.now());
        added3Task.setDuration(Duration.ofMinutes(15));
        added4Task.setStartTime(LocalDateTime.now());
        added4Task.setDuration(Duration.ofMinutes(15));

        taskManager.saveTask(added3Task);
        taskManager.saveTask(added4Task);

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Список задач пуст");
        assertEquals(2, tasks.size(), "Неверное количество задач");
    }

    @Test
    public void getHistoryShouldSuccessfullySaveTaskGetHistoryTest() {
        final Task added5Task = new Task("Задача 1", "Сдать спринт9", Status.NEW);
        added5Task.setStartTime(LocalDateTime.now());
        added5Task.setDuration(Duration.ofMinutes(15));
        taskManager.saveTask(added5Task);

        taskManager.getTaskById(added5Task.getId());
        final List<Task> history = taskManager.getHistory();

        assertEquals(1, history.size(), "Неверный размер истории");
        assertEquals(added5Task, history.getFirst(), "Задачи не совпадают");
    }

    @Test
    public void getPrioritizedTasksShouldSuccessfullySaveTasksGetPrioritizedTasksTest() {
        final Task added6Task = new Task("Задача 1", "Сдать Модуль 2", Status.IN_PROGRESS);
        added6Task.setStartTime(LocalDateTime.now());
        added6Task.setDuration(Duration.ofMinutes(15));
        taskManager.saveTask(added6Task);

        final List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();

        assertEquals(1, prioritizedTasks.size(), "Неверный размер отсортированных задач");
        assertEquals(added6Task, prioritizedTasks.getFirst(), "Задачи не совпадают");
    }

    @Test
    public void getTasksShouldBeEmptySaveTaskDeleteTaskByIdGetTasksTest() {
        final Task added7Task = new Task("Задача 7", "Сдать Модуль 8", Status.IN_PROGRESS);
        added7Task.setStartTime(LocalDateTime.now());
        added7Task.setDuration(Duration.ofMinutes(15));
        taskManager.saveTask(added7Task);
        taskManager.deleteTaskById(added7Task.getId());

        final List<Task> emptyListAfterDeleteTasks = taskManager.getTasks();

        assertEquals(0, emptyListAfterDeleteTasks.size(), "Лист не пустой");
    }

    @Test
    public void getSubtasksShouldBeEmptySaveSubtaskDeleteSubtaskByIdGetSubtasksTest() {
        final Epic added1Epic = new Epic("E1", "Сдать Модуль 8");
        taskManager.saveEpic(added1Epic);
        final Subtask subtask1 = new Subtask("S1", "D1");
        subtask1.setStartTime(LocalDateTime.now());
        subtask1.setDuration(Duration.ofMinutes(15));
        subtask1.setEpicIdForThisSubtask(added1Epic.getId());
        taskManager.saveSubtask(subtask1);

        taskManager.deleteSubtaskById(subtask1.getId());

        final List<Subtask> emptyListAfterDeleteSubtasks = taskManager.getSubtasks();

        assertEquals(0, emptyListAfterDeleteSubtasks.size(), "Лист не пустой");
    }

    @Test
    public void getEpicsShouldBeEmptySaveEpicDeleteEpicByIdGetEpicsTest() {
        final Epic added1Epic = new Epic("E1", "Сдать Модуль 8");
        taskManager.saveEpic(added1Epic);
        final Subtask subtask1 = new Subtask("S1", "D1");
        subtask1.setStartTime(LocalDateTime.now());
        subtask1.setDuration(Duration.ofMinutes(15));
        subtask1.setEpicIdForThisSubtask(added1Epic.getId());
        taskManager.saveSubtask(subtask1);

        taskManager.deleteEpicById(added1Epic.getId());

        final List<Epic> emptyListAfterDeleteEpics = taskManager.getEpics();

        assertEquals(0, emptyListAfterDeleteEpics.size(), "Лист не пустой");
    }

    @Test
    public void deleteTasksShouldSuccessfullySaveTaskDeleteTasksGetTasksTest() {
        final Task added8Task = new Task("Задача 8", "Сдать Модуль 8", Status.IN_PROGRESS);
        added8Task.setStartTime(LocalDateTime.now());
        added8Task.setDuration(Duration.ofMinutes(15));
        taskManager.saveTask(added8Task);
        taskManager.deleteTasks();

        final List<Task> emptyListAfterDeleteTasks = taskManager.getTasks();

        assertEquals(0, emptyListAfterDeleteTasks.size(), "Задачи не удалились");

    }

    @Test
    public void deleteEpicsDeleteSubtasksShouldSuccessfullySaveEpicSaveSubtaskDeleteEpicsDeleteSubtasksTest() {
        final Epic added1Epic = new Epic("E1", "Сдать Модуль 8");
        taskManager.saveEpic(added1Epic);
        final Subtask subtask1 = new Subtask("S1", "D1");
        subtask1.setStartTime(LocalDateTime.now());
        subtask1.setDuration(Duration.ofMinutes(15));
        subtask1.setEpicIdForThisSubtask(added1Epic.getId());
        taskManager.saveSubtask(subtask1);
        taskManager.deleteSubtasks();

        final List<Subtask> emptyListAfterDeleteSubtasks = taskManager.getSubtasks();

        assertEquals(0, emptyListAfterDeleteSubtasks.size(), "Подзадачи не удалились");

        taskManager.deleteEpics();

        final List<Epic> emptyListAfterDeleteEpics = taskManager.getEpics();

        assertEquals(0, emptyListAfterDeleteEpics.size(), "Эпики не удалились");
    }

}