package ru.practicum.tasksManager.service.impl;

import org.junit.jupiter.api.Test;
import ru.practicum.tasksManager.model.Status;
import ru.practicum.tasksManager.model.Task;
import ru.practicum.tasksManager.service.HistoryManager;
import ru.practicum.tasksManager.utilities.Managers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {

    @Test
    void checkForDuplicationInHistory() {
        HistoryManager historyManager = Managers.getDefaultHistory();
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
    void shouldReturnEmptyHistoryListAfterDeleteTaskAfterSave() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        historyManager.addToHistory(task);
        historyManager.removeFromHistory(task.getId());
        final List<Task> history = historyManager.getHistory();
        assertEquals(0, history.size(), "История не пустая.");
    }

}