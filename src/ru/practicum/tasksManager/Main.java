package ru.practicum.tasksManager;

import ru.practicum.tasksManager.model.Epic;
import ru.practicum.tasksManager.model.Status;
import ru.practicum.tasksManager.model.Subtask;
import ru.practicum.tasksManager.model.Task;
import ru.practicum.tasksManager.service.TaskManager;
import ru.practicum.tasksManager.utilities.Managers;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager inMemoryTaskManager = Managers.getDefault();

        Task task1 = new Task("Task1", "Desc1", Status.NEW);
        Task task2 = new Task("Task2", "Desc2", Status.NEW);
        Task task3 = new Task("Task3", "Desc3", Status.NEW);

        inMemoryTaskManager.saveTask(task1);
        inMemoryTaskManager.saveTask(task2);
        inMemoryTaskManager.saveTask(task3);

        Task task1Clone = (Task) task1.clone();
        Task task2Clone = (Task) task2.clone();

        task1Clone.setStatus(Status.IN_PROGRESS);
        task2Clone.setStatus(Status.DONE);

        inMemoryTaskManager.updateTask(task1Clone);
        inMemoryTaskManager.updateTask(task2Clone);

        System.out.println(inMemoryTaskManager.getTasks());

        Epic epic1 = new Epic("Epic1", "Desc1");
        Epic epic2 = new Epic("Epic2", "Desc2");

        Subtask subtask1 = new Subtask("Subtask1", "Desk1");
        Subtask subtask2 = new Subtask("Subtask2", "Desk2");
        Subtask subtask3 = new Subtask("Subtask3", "Desk3");


        inMemoryTaskManager.saveEpic(epic1);
        inMemoryTaskManager.saveEpic(epic2);
        subtask1.setEpicIdForThisSubtask(epic1.getId());
        subtask2.setEpicIdForThisSubtask(epic1.getId());
        subtask3.setEpicIdForThisSubtask(epic1.getId());

        inMemoryTaskManager.saveSubtask(subtask1);
        inMemoryTaskManager.saveSubtask(subtask2);
        inMemoryTaskManager.saveSubtask(subtask3);

        System.out.println(inMemoryTaskManager.getSubtasks());
        System.out.println();

        Subtask subtask1Clone = (Subtask) subtask1.clone();
        Subtask subtask3Clone = (Subtask) subtask3.clone();

        subtask1Clone.setStatus(Status.IN_PROGRESS);
        subtask3Clone.setStatus(Status.DONE);
        inMemoryTaskManager.updateSubtask(subtask1Clone);
        inMemoryTaskManager.updateSubtask(subtask3Clone);

        System.out.println(inMemoryTaskManager.getEpics());
        System.out.println(inMemoryTaskManager.getSubtasks());
        System.out.println();


        inMemoryTaskManager.getEpicById(epic1.getId());
        inMemoryTaskManager.getEpicById(epic1.getId());
        inMemoryTaskManager.getEpicById(epic1.getId());
        inMemoryTaskManager.getEpicById(epic2.getId());
        inMemoryTaskManager.getTaskById(task1.getId());
        inMemoryTaskManager.getTaskById(task2.getId());
        inMemoryTaskManager.getTaskById(task3.getId());
        inMemoryTaskManager.getEpicById(epic1.getId());
        inMemoryTaskManager.getSubtaskById(subtask2.getId());
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println();

        inMemoryTaskManager.deleteById(epic1.getId());
        System.out.println(inMemoryTaskManager.getHistory());

    }

}
