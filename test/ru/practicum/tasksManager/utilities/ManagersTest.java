package ru.practicum.tasksManager.utilities;

import org.junit.jupiter.api.Test;
import ru.practicum.tasksManager.interfaces.HistoryManager;
import ru.practicum.tasksManager.interfaces.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void newTaskManager(){
        TaskManager taskManager = Managers.getDefault();

        assertNotNull(taskManager,"Менеджер не проинициализирован");
    }

    @Test
    void newHistoryManager(){
        HistoryManager historyManager = Managers.getDefaultHistory();

        assertNotNull(historyManager,"Менеджер не проинициализирован");
    }

}