package ru.practicum.tasksManager.service;

import ru.practicum.tasksManager.model.Epic;
import ru.practicum.tasksManager.model.Subtask;
import ru.practicum.tasksManager.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskManager {

    void saveTask(Task task);

    void saveSubtask(Subtask subtask);

    void saveEpic(Epic epic);

    List<Task> getTasks();

    List<Subtask> getSubtasks();

    List<Epic> getEpics();

    Optional<Task> getTaskById(int id);

    Optional<Subtask> getSubtaskById(int id);

    Optional<Epic> getEpicById(int id);

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
