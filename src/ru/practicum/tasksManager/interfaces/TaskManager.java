package ru.practicum.tasksManager.interfaces;

import ru.practicum.tasksManager.model.Epic;
import ru.practicum.tasksManager.model.Subtask;
import ru.practicum.tasksManager.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public interface TaskManager {

    void saveTask(Task task);

    void saveSubtask(Subtask subtask);

    void saveEpic(Epic epic);

    ArrayList<Task> getTasks();

    ArrayList<Subtask> getSubtasks();

    ArrayList<Epic> getEpics();

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

    ArrayList<Subtask> getAllSubtasksByEpic(int id);

    LinkedList<Task> getHistory();

    void setEpicIdToSub(int id, Subtask subtask);

}
