package ru.practicum.tasksManager.service.impl;

import org.junit.jupiter.api.Test;
import ru.practicum.tasksManager.model.Epic;
import ru.practicum.tasksManager.model.Status;
import ru.practicum.tasksManager.model.Subtask;
import ru.practicum.tasksManager.model.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {


    @Test
    void equalsTwoTasksWithOneId() {
        Task taskOne = new Task("Task1", "Desc1", Status.NEW);
        Task taskTwo = new Task("Task2", "Desc2", Status.IN_PROGRESS);

        taskOne.setId(1);
        taskTwo.setId(1);

        assertEquals(taskOne, taskTwo, "Ошибка сравнения по ID");

    }

    @Test
    void equalsTwoEpicsWithOneId() {
        Epic epicOne = new Epic("epic1", "Desc1");
        Epic epicTwo = new Epic("epic2", "Desc2");

        epicOne.setId(1);
        epicTwo.setId(1);

        assertEquals(epicOne, epicTwo, "Ошибка сравнения по ID");

    }

    @Test
    void equalsTwoSubtaskWithOneId() {
        Subtask subtaskOne = new Subtask("Subtask1", "Desc1");
        Subtask subtaskTwo = new Subtask("Subtask2", "Desc2");

        subtaskOne.setId(1);
        subtaskTwo.setId(1);

        assertEquals(subtaskOne, subtaskTwo, "Ошибка сравнения по ID");

    }

}