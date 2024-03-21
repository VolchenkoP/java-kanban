package ru.practicum.tasksManager.model;

import org.junit.jupiter.api.Test;
import ru.practicum.tasksManager.interfaces.TaskManager;
import ru.practicum.tasksManager.utilities.Managers;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

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