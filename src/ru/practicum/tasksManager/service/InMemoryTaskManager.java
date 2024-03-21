package ru.practicum.tasksManager.service;

import ru.practicum.tasksManager.interfaces.HistoryManager;
import ru.practicum.tasksManager.interfaces.TaskManager;
import ru.practicum.tasksManager.model.*;
import ru.practicum.tasksManager.model.Status;
import ru.practicum.tasksManager.utilities.Managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private int countId;
    private final Map<Integer, Task> tasks;
    private final Map<Integer, Subtask> subtasks;
    private final Map<Integer, Epic> epics;

    HistoryManager historyManager;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
        countId = 0;
    }

    @Override
    public void saveTask(Task task) {
        if (!tasks.containsValue(task)) {
            countId++;
            task.setId(countId);
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Такая задача уже создана");
        }
    }

    @Override
    public void saveSubtask(Subtask subtask) {
        if (!subtasks.containsValue(subtask)) {
            countId++;
            subtask.setId(countId);
            Epic epic = epics.get(subtask.getEpicIdForThisSubtask());
            if (epic == null) {
                return;
            }
            epic.addSubtasksForThisEpic(subtask);
            changeEpicStatus(epic);
            subtasks.put(subtask.getId(), subtask);
        } else {
            System.out.println("Такая подзадача уже создана");
        }

    }

    @Override
    public void saveEpic(Epic epic) {
        if (!epics.containsValue(epic)) {
            countId++;
            epic.setId(countId);
            epics.put(epic.getId(), epic);
        } else {
            System.out.println("Такой эпик уже создан");
        }
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public void deleteById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (epics.containsKey(id)) {
            HashMap<Integer, Subtask> epicSubtasks = epics.get(id).getSubtasksForThisEpic();
            for (Integer subtasksId : epicSubtasks.keySet()) {
                subtasks.remove(subtasksId);
            }
            epics.remove(id);
        } else if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.get(id);
            Epic epic = epics.get(subtask.getEpicIdForThisSubtask());
            epic.deleteSubtaskForThisEpic(id);
            changeEpicStatus(epic);
            subtasks.remove(id);
        }
    }

    @Override
    public void updateTask(Task task) {
        Task taskLink = tasks.get(task.getId());
        historyManager.add(taskLink);
        taskLink.setName(task.getName());
        taskLink.setDescription(task.getDescription());
        taskLink.setStatus(task.getStatus());
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Subtask subtaskLink = subtasks.get(subtask.getId());
        historyManager.add(subtaskLink);
        subtaskLink.setName(subtask.getName());
        subtaskLink.setDescription(subtask.getDescription());
        subtaskLink.setStatus(subtask.getStatus());
        changeEpicStatus(epics.get(subtask.getEpicIdForThisSubtask()));
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic epicLink = epics.get(epic.getId());
        historyManager.add(epicLink);
        epicLink.setName(epic.getName());
        epicLink.setDescription(epic.getDescription());
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    public void deleteSubtasks() {
        for (Epic epic : epics.values()) {
            epic.getSubtasksForThisEpic().clear();
            changeEpicStatus(epic);
        }
        subtasks.clear();
    }

    @Override
    public void deleteEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public ArrayList<Subtask> getAllSubtasksByEpic(int id) {
        HashMap<Integer, Subtask> subtasksByEpic = epics.get(id).getSubtasksForThisEpic();
        return new ArrayList<>(subtasksByEpic.values());
    }

    @Override
    public LinkedList<Task> getHistory(){
        return historyManager.getHistory();
    }

    @Override
    public  void setEpicIdToSub(int id, Subtask subtask){
        if(epics.containsKey(id)){
            subtask.setEpicIdForThisSubtask(id);
        } else {
            System.out.println("Данный ID не принадлежит Эпику");
        }
    }

    private void changeEpicStatus(Epic epic) {
        if (epic.getSubtasksForThisEpic().isEmpty()) {
            epic.setStatus(Status.NEW);
        } else {
            int countNew = 0;
            int countDone = 0;
            HashMap<Integer, Subtask> forStatus = epic.getSubtasksForThisEpic();
            for (Subtask subtaskCopy : forStatus.values()) {
                if (subtaskCopy.getStatus() == Status.IN_PROGRESS) {
                    epic.setStatus(Status.IN_PROGRESS);
                    return;
                } else if (subtaskCopy.getStatus() == Status.NEW) {
                    countNew++;
                } else if (subtaskCopy.getStatus() == Status.DONE) {
                    countDone++;
                }
            }
            if (countNew == forStatus.size()) {
                epic.setStatus(Status.NEW);
            } else if (countDone == forStatus.size()) {
                epic.setStatus(Status.DONE);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }

}
