package ru.practicum.tasksManager.model;

import org.junit.jupiter.api.Test;
import ru.practicum.tasksManager.service.TaskManager;
import ru.practicum.tasksManager.utilities.Managers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TaskTest {

    @Test
    void saveTaskShouldSuccessfullySaveTaskTest() {
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


}