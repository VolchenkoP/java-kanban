package ru.practicum.tasksManager.service;

import ru.practicum.tasksManager.model.Epic;
import ru.practicum.tasksManager.model.Subtask;
import ru.practicum.tasksManager.model.Task;

import java.util.List;

public interface TaskManager {

    void saveTask(Task task);

    void saveSubtask(Subtask subtask);

    void saveEpic(Epic epic);

    List<Task> getTasks();

    List<Subtask> getSubtasks();

    List<Epic> getEpics();

    Task getTaskById(int id);

    Subtask getSubtaskById(int id);

    Epic getEpicById(int id);

    void deleteById(int id);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    void deleteTasks();

    void deleteSubtasks();

    void deleteEpics();

    int getCountId();

    List<Subtask> getAllSubtasksByEpic(int id);

    List<Task> getHistory();


}
