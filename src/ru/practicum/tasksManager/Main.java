package ru.practicum.tasksManager;

import ru.practicum.tasksManager.model.Epic;
import ru.practicum.tasksManager.model.Status;
import ru.practicum.tasksManager.model.Subtask;
import ru.practicum.tasksManager.model.Task;
import ru.practicum.tasksManager.service.impl.FileBackedTaskManager;
import ru.practicum.tasksManager.utilities.Managers;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        /* TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("Task1", "Desc1", Status.NEW);
        Task task2 = new Task("Task2", "Desc2", Status.NEW);
        Task task3 = new Task("Task3", "Desc3", Status.NEW);

        taskManager.saveTask(task1);
        taskManager.saveTask(task2);
        taskManager.saveTask(task3);

        Task task1Clone = (Task) task1.clone();
        Task task2Clone = (Task) task2.clone();

        task1Clone.setStatus(Status.IN_PROGRESS);
        task2Clone.setStatus(Status.DONE);

        taskManager.updateTask(task1Clone);
        taskManager.updateTask(task2Clone);

        System.out.println(taskManager.getTasks());

        Epic epic1 = new Epic("Epic1", "Desc1");
        Epic epic2 = new Epic("Epic2", "Desc2");

        Subtask subtask1 = new Subtask("Subtask1", "Desk1");
        Subtask subtask2 = new Subtask("Subtask2", "Desk2");
        Subtask subtask3 = new Subtask("Subtask3", "Desk3");

        taskManager.saveEpic(epic1);
        taskManager.saveEpic(epic2);
        subtask1.setEpicIdForThisSubtask(epic1.getId());
        subtask2.setEpicIdForThisSubtask(epic1.getId());
        subtask3.setEpicIdForThisSubtask(epic1.getId());

        taskManager.saveSubtask(subtask1);
        taskManager.saveSubtask(subtask2);
        taskManager.saveSubtask(subtask3);

        System.out.println(taskManager.getSubtasks());
        System.out.println();

        Subtask subtask1Clone = (Subtask) subtask1.clone();
        Subtask subtask3Clone = (Subtask) subtask3.clone();

        subtask1Clone.setStatus(Status.IN_PROGRESS);
        subtask3Clone.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask1Clone);
        taskManager.updateSubtask(subtask3Clone);

        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        System.out.println();

        taskManager.getEpicById(epic1.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getEpicById(epic2.getId());
        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getTaskById(task3.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getSubtaskById(subtask2.getId());
        System.out.println(taskManager.getHistory());
        System.out.println();

        taskManager.deleteById(epic1.getId());
        System.out.println(taskManager.getHistory());
        System.out.println();

        taskManager.deleteEpics();
        System.out.println(taskManager.getHistory()); */

        File file = new File("/Users/pavelvolcenko/Documents", "data.txt");
        FileBackedTaskManager fileTaskManager = Managers.getDefaultFileManager(file);

        Task task1 = new Task("Task1", "Desc1", Status.NEW);
        Task task2 = new Task("Task2", "Desc2", Status.NEW);
        Task task3 = new Task("Task3", "Desc3", Status.NEW);

        fileTaskManager.saveTask(task1);
        fileTaskManager.saveTask(task2);
        fileTaskManager.saveTask(task3);

        Task task1Clone = (Task) task1.clone();
        Task task2Clone = (Task) task2.clone();

        task1Clone.setStatus(Status.IN_PROGRESS);
        task2Clone.setStatus(Status.DONE);

        fileTaskManager.updateTask(task1Clone);
        fileTaskManager.updateTask(task2Clone);

        Epic epic1 = new Epic("Epic1", "Desc1");
        Epic epic2 = new Epic("Epic2", "Desc2");

        Subtask subtask1 = new Subtask("Subtask1", "Desk1");
        Subtask subtask2 = new Subtask("Subtask2", "Desk2");
        Subtask subtask3 = new Subtask("Subtask3", "Desk3");

        fileTaskManager.saveEpic(epic1);
        fileTaskManager.saveEpic(epic2);
        subtask1.setEpicIdForThisSubtask(epic1.getId());
        subtask2.setEpicIdForThisSubtask(epic1.getId());
        subtask3.setEpicIdForThisSubtask(epic2.getId());

        fileTaskManager.saveSubtask(subtask1);
        fileTaskManager.saveSubtask(subtask2);
        fileTaskManager.saveSubtask(subtask3);

        fileTaskManager.getEpicById(epic1.getId());
        fileTaskManager.getSubtaskById(subtask1.getId());
        Task task22 = new Task("Task1", "Desc1", Status.NEW);
        ;

        fileTaskManager.saveTask(task22);

        System.out.println(fileTaskManager.getTasks());
        System.out.println(fileTaskManager.getEpics());
        System.out.println(fileTaskManager.getSubtasks());

        FileBackedTaskManager fileTaskManager2 = Managers.getLoadedFileManager(file);

        System.out.println(fileTaskManager2.getTasks());
        System.out.println(fileTaskManager2.getEpics());
        System.out.println(fileTaskManager2.getSubtasks());

    }

}
