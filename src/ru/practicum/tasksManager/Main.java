package ru.practicum.tasksManager;

import ru.practicum.tasksManager.model.*;
import ru.practicum.tasksManager.service.TaskManager;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("Task1", "Desc1", Status.NEW);
        Task task2 = new Task("Task2", "Desc2", Status.NEW);

        taskManager.saveTask(task1);
        taskManager.saveTask(task2);

        Epic epic1 = new Epic("Epic1", "Desc1");
        Epic epic2 = new Epic("Epic2", "Desc2");

        Subtask subtask1 = new Subtask("Subtask1", "Desk1");
        Subtask subtask2 = new Subtask("Subtask2", "Desk2");
        Subtask subtask3 = new Subtask("Subtask3", "Desk3");


        taskManager.saveEpic(epic1);
        taskManager.saveEpic(epic2);
        subtask1.setEpicIdForThisSubtask(epic1.getId());
        subtask2.setEpicIdForThisSubtask(epic1.getId());
        subtask3.setEpicIdForThisSubtask(epic2.getId());

        taskManager.saveSubtask(subtask1);
        taskManager.saveSubtask(subtask2);
        taskManager.saveSubtask(subtask3);

        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.NEW);
        subtask3.setStatus(Status.DONE);

        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);
        taskManager.updateSubtask(subtask3);

        taskManager.deleteSubtasks();

    }

}
