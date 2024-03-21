package ru.practicum.tasksManager.service;

import org.junit.jupiter.api.Test;
import ru.practicum.tasksManager.interfaces.HistoryManager;
import ru.practicum.tasksManager.interfaces.TaskManager;
import ru.practicum.tasksManager.model.Epic;
import ru.practicum.tasksManager.model.Status;
import ru.practicum.tasksManager.model.Subtask;
import ru.practicum.tasksManager.model.Task;
import ru.practicum.tasksManager.utilities.Managers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest  {

    @Test
    void checkHistoryTaskEqualsOldTask(){
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        taskManager.saveTask(task);
        historyManager.add(task);

        Task cloneTask = (Task) task.clone();
        cloneTask.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(cloneTask);

        final List<Task> tasks = taskManager.getTasks();
        final List<Task> history = historyManager.getHistory();

        assertNotEquals(tasks.getFirst().getStatus(),history.getFirst().getStatus(),"Обновление не произошло");
        assertEquals(Status.NEW,history.getFirst().getStatus(),"Задача неправильно сохранилась");

    }

    @Test
    void newManagers(){
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        assertNotNull(taskManager,"Менеджер не проинициализирован");
        assertNotNull(historyManager,"Менеджер не проинициализирован");
    }

    @Test
    void addNewTask() {
        TaskManager taskManager = Managers.getDefault();
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        taskManager.saveTask(task);
        final int taskId = task.getId();

        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.getFirst(), "Задачи не совпадают.");
    }

    @Test
    void add() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void equalsTwoTasksWithOneId(){
        Task taskOne = new Task("Task1", "Desc1",Status.NEW);
        Task taskTwo = new Task("Task2", "Desc2",Status.IN_PROGRESS);

        taskOne.setId(1);
        taskTwo.setId(1);

        assertEquals(taskOne, taskTwo, "Ошибка сравнения по ID");

    }
    @Test
    void equalsTwoSubtasksWithOneId(){
        Subtask subtaskOne = new Subtask("Subtask1", "Desc1");
        Subtask subtaskTwo = new Subtask("Subtask2", "Desc2");

        subtaskOne.setId(1);
        subtaskTwo.setId(1);

        assertEquals(subtaskOne, subtaskTwo, "Ошибка сравнения по ID");

    }
    @Test
    void equalsTwoEpicsWithOneId(){
        Epic epicOne = new Epic("epic1", "Desc1");
        Epic epicTwo = new Epic("epic2", "Desc2");

        epicOne.setId(1);
        epicTwo.setId(1);

        assertEquals(epicOne, epicTwo, "Ошибка сравнения по ID");

    }

    @Test
    void checkSubtaskLikeEpicForSubtask(){
        TaskManager taskManager = Managers.getDefault();
        Subtask subtaskOne = new Subtask("Sub1", "Desc1");
        Subtask subtaskTwo = new Subtask("Sub2", "Desc3");

        taskManager.saveSubtask(subtaskOne);
        taskManager.saveSubtask(subtaskTwo);

        final int subtaskTwoId = subtaskTwo.getId();

        taskManager.setEpicIdToSub(subtaskTwoId,subtaskOne);

        final int idEpicFromSubtaskOne = subtaskOne.getEpicIdForThisSubtask();

        assertEquals(0, idEpicFromSubtaskOne, "Подзадача смогла стать Эпиком для другой подзадачи.");

    }

}