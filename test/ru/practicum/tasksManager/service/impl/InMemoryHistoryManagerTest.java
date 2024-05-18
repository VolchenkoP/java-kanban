package ru.practicum.tasksManager.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.tasksManager.model.Status;
import ru.practicum.tasksManager.model.Task;
import ru.practicum.tasksManager.service.HistoryManager;
import ru.practicum.tasksManager.utilities.Managers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    protected HistoryManager historyManager;

    @BeforeEach
    public void init() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void addToHistoryShouldSuccessfullyAddTaskToHistoryTest() {
        final Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        historyManager.addToHistory(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
        assertTrue(task.equals(history.get(0)), "Задачи не совпадают");
    }

    @Test
    void addToHistoryShouldSuccessfullyExceptionDuplicatesAddDuplicatesToHistoryTest() {
        Task task = new Task("title", "desc", Status.NEW);
        final int sizeForCheckRequestSize = 19;
        for (int i = 0; i <= sizeForCheckRequestSize; i++) {
            historyManager.addToHistory(task);
        }
        List<Task> exampleOfRequestHistoryList = historyManager.getHistory();

        assertEquals(1, exampleOfRequestHistoryList.size(), "Ограничение дублирования "
                + "не работает");
    }


    @Test
    void removeFromHistoryShouldSuccessfullyRemoveTaskFromHistoryTest() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        historyManager.addToHistory(task);
        historyManager.removeFromHistory(task.getId());
        final List<Task> history = historyManager.getHistory();
        assertEquals(0, history.size(), "История не пустая.");
    }

}