package ru.practicum.tasksManager.service.impl;

import org.junit.jupiter.api.Test;
import ru.practicum.tasksManager.model.Status;
import ru.practicum.tasksManager.model.Task;
import ru.practicum.tasksManager.service.HistoryManager;
import ru.practicum.tasksManager.utilities.Managers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InMemoryHistoryManagerTest {

    @Test
    void checkSizeOfRequestHistory() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task = new Task("title", "desc", Status.NEW);
        final int sizeFromRequestHistoryShouldBe = 10;
        final int sizeForCheckRequestSize = 19;
        for (int i = 0; i <= sizeForCheckRequestSize; i++) {
            historyManager.addToHistory(task);
        }
        List<Task> exampleOfRequestHistoryList = historyManager.getHistory();

        assertEquals(sizeFromRequestHistoryShouldBe, exampleOfRequestHistoryList.size(), "Ограничение листа "
                + "не работает");
    }

    @Test
    void add() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        historyManager.addToHistory(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

}