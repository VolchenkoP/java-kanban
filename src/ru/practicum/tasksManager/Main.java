package ru.practicum.tasksManager;

import ru.practicum.tasksManager.model.Epic;
import ru.practicum.tasksManager.model.Status;
import ru.practicum.tasksManager.model.Subtask;
import ru.practicum.tasksManager.model.Task;
import ru.practicum.tasksManager.service.impl.FileBackedTaskManager;
import ru.practicum.tasksManager.utilities.Managers;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Поехали!");
        /*
        TaskManager taskManager = Managers.getDefault();

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
        subtask3.setEpicIdForThisSubtask(epic2.getId());

        taskManager.saveSubtask(subtask1);
        taskManager.saveSubtask(subtask2);
        taskManager.saveSubtask(subtask3);

        System.out.println(taskManager.getSubtasks());
        System.out.println();

        Subtask subtask1Clone = (Subtask) subtask1.clone();
        Subtask subtask3Clone = (Subtask) subtask3.clone();

        System.out.println("Epics before subtask update: " + taskManager.getEpics());

        subtask1Clone.setStatus(Status.IN_PROGRESS);
        subtask3Clone.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask1Clone);
        taskManager.updateSubtask(subtask3Clone);

        System.out.println("Epics after update subtasks: " + taskManager.getEpics());

        System.out.println("Subtasks: " + taskManager.getSubtasks());

        System.out.println();

        taskManager.getEpicById(epic1.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getEpicById(epic2.getId());
        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getTaskById(task3.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getSubtaskById(subtask1.getId());
        taskManager.getSubtaskById(subtask2.getId());
        taskManager.getSubtaskById(subtask3.getId());
        System.out.println(taskManager.getHistory());
        System.out.println();

        taskManager.deleteById(epic1.getId());
        System.out.println("History after delete epic1: " + taskManager.getHistory());
        System.out.println("Subtasks after delete epic1: " + taskManager.getSubtasks());
        System.out.println();

        taskManager.deleteEpics();
        System.out.println(taskManager.getHistory()); */

        File file = new File("/Users/pavelvolcenko/Documents", "data.txt");
        FileBackedTaskManager fileTaskManager = Managers.getDefaultFileManager(file);

        Task task1 = new Task("Task1", "Desc1", Status.NEW);
        task1.setStartTime(LocalDateTime.now().plusMinutes(new Random().nextInt(100)));
        task1.setDuration(Duration.ofMinutes(15));
        Task task2 = new Task("Task2", "Desc2", Status.NEW);
        task2.setStartTime(LocalDateTime.now().plusMinutes(new Random().nextInt(100)));
        task2.setDuration(Duration.ofMinutes(15));
        Task task3 = new Task("Task3", "Desc3", Status.NEW);
        task3.setStartTime(LocalDateTime.now().plusMinutes(new Random().nextInt(100)));
        task3.setDuration(Duration.ofMinutes(15));

        fileTaskManager.saveTask(task1);
        fileTaskManager.saveTask(task2);
        fileTaskManager.saveTask(task3);
        System.out.println(task1.getEndTime());

        Task task1Clone = (Task) task1.clone();
        Task task2Clone = (Task) task2.clone();

        task1Clone.setStatus(Status.IN_PROGRESS);
        task2Clone.setStatus(Status.DONE);

        fileTaskManager.updateTask(task1Clone);
        fileTaskManager.updateTask(task2Clone);

        Epic epic1 = new Epic("Epic1", "Desc1");
        Epic epic2 = new Epic("Epic2", "Desc2");

        Subtask subtask1 = new Subtask("Subtask1", "Desk1");
        subtask1.setStartTime(LocalDateTime.now().plusMinutes(new Random().nextInt(100)));
        subtask1.setDuration(Duration.ofMinutes(15));

        Subtask subtask2 = new Subtask("Subtask2", "Desk2");
        subtask2.setStartTime(LocalDateTime.now().plusMinutes(new Random().nextInt(100)));
        subtask2.setDuration(Duration.ofMinutes(15));

        Subtask subtask3 = new Subtask("Subtask3", "Desk3");
        subtask3.setStartTime(LocalDateTime.now().plusMinutes(new Random().nextInt(100)));
        subtask3.setDuration(Duration.ofMinutes(15));

        fileTaskManager.saveEpic(epic1);
        fileTaskManager.saveEpic(epic2);
        subtask1.setEpicIdForThisSubtask(epic1.getId());
        subtask2.setEpicIdForThisSubtask(epic1.getId());
        subtask3.setEpicIdForThisSubtask(epic2.getId());

        fileTaskManager.saveSubtask(subtask1);
        fileTaskManager.saveSubtask(subtask2);
        fileTaskManager.saveSubtask(subtask3);

        System.out.println(subtask1.getEndTime());
        System.out.println(epic1.getEndTime());

        fileTaskManager.getEpicById(epic1.getId());
        fileTaskManager.getSubtaskById(subtask1.getId());
        Task task22 = new Task("Task22", "Desc22", Status.NEW);
        task22.setStartTime(LocalDateTime.now().plusMinutes(new Random().nextInt(100)));
        task22.setDuration(Duration.ofMinutes(15));

        fileTaskManager.saveTask(task22);

        System.out.println(fileTaskManager.getTasks());
        System.out.println(fileTaskManager.getEpics());
        System.out.println(fileTaskManager.getSubtasks());

        System.out.println(fileTaskManager.getPrioritizedTasks());

        fileTaskManager.getTaskById(1);

        System.out.println();
        System.out.println("History: " + fileTaskManager.getHistory());

        FileBackedTaskManager fileTaskManager2 = Managers.getLoadedFileManager(file);

        System.out.println(fileTaskManager2.getTasks());
        System.out.println(fileTaskManager2.getEpics());
        System.out.println(fileTaskManager2.getSubtasks());
        System.out.println("Subtasks by Epic1 after load" + fileTaskManager2.getEpicById(4).getSubtasksForThisEpic().values());

        subtask1.setStatus(Status.DONE);
        fileTaskManager2.updateSubtask(subtask1);
        System.out.println("Subtasks after update st1 status: " + fileTaskManager2.getSubtasks());
        System.out.println("Epics after update st1 status: " + fileTaskManager2.getEpics());

        fileTaskManager2.deleteSubtaskById(6);
        System.out.println("Subtasks after delete st1 :" + fileTaskManager2.getSubtasks());
        System.out.println("Epics after delete st1: " + fileTaskManager2.getEpics());

    }

}
