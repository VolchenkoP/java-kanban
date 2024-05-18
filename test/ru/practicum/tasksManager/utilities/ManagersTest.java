package ru.practicum.tasksManager.utilities;

import org.junit.jupiter.api.Test;
import ru.practicum.tasksManager.service.HistoryManager;
import ru.practicum.tasksManager.service.TaskManager;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ManagersTest {

    @Test
    void createTaskManagerShouldSuccessfullyCreateTaskManagerFromManagersTest() {
        final TaskManager taskManager = Managers.getDefault();

        assertNotNull(taskManager, "Менеджер не проинициализирован");
    }

    @Test
    void createHistoryManagerShouldSuccessfullyCreateHistoryManagerFromManagersTest() {
        final HistoryManager historyManager = Managers.getDefaultHistory();

        assertNotNull(historyManager, "Менеджер не проинициализирован");
    }

}