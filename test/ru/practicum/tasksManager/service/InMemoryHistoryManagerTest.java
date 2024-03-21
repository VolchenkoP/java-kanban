package ru.practicum.tasksManager.service;

import org.junit.jupiter.api.Test;
import ru.practicum.tasksManager.interfaces.HistoryManager;
import ru.practicum.tasksManager.interfaces.TaskManager;
import ru.practicum.tasksManager.model.Status;
import ru.practicum.tasksManager.model.Task;
import ru.practicum.tasksManager.utilities.Managers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

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
    void add() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

}