package ru.practicum.tasksManager.service.impl;

import ru.practicum.tasksManager.model.Epic;
import ru.practicum.tasksManager.model.Status;
import ru.practicum.tasksManager.model.Subtask;
import ru.practicum.tasksManager.model.Task;
import ru.practicum.tasksManager.service.HistoryManager;
import ru.practicum.tasksManager.service.TaskManager;
import ru.practicum.tasksManager.utilities.Managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks;
    private final Map<Integer, Subtask> subtasks;
    private final Map<Integer, Epic> epics;
    private final HistoryManager historyManager;
    private int countId;

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
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public Task getTaskById(int id) {
        Task taskFromMap = tasks.get(id);
        historyManager.addToHistory(taskFromMap);
        return taskFromMap;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtaskFromMap = subtasks.get(id);
        historyManager.addToHistory(subtaskFromMap);
        return subtaskFromMap;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epicFromMap = epics.get(id);
        historyManager.addToHistory(epicFromMap);
        return epicFromMap;
    }

    @Override
    public void deleteById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (epics.containsKey(id)) {
            Map<Integer, Subtask> epicSubtasks = epics.get(id).getSubtasksForThisEpic();
            for (Integer subtasksId : epicSubtasks.keySet()) {
                subtasks.remove(subtasksId);
                historyManager.removeFromHistory(subtasksId);
            }
            epics.remove(id);
        } else if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.get(id);
            Epic epic = epics.get(subtask.getEpicIdForThisSubtask());
            epic.deleteSubtaskForThisEpic(id);
            changeEpicStatus(epic);
            subtasks.remove(id);
        }
        historyManager.removeFromHistory(id);
    }

    @Override
    public void updateTask(Task task) {
        Task taskLink = tasks.get(task.getId());
        taskLink.setName(task.getName());
        taskLink.setDescription(task.getDescription());
        taskLink.setStatus(task.getStatus());
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Subtask subtaskLink = subtasks.get(subtask.getId());
        subtaskLink.setName(subtask.getName());
        subtaskLink.setDescription(subtask.getDescription());
        subtaskLink.setStatus(subtask.getStatus());
        changeEpicStatus(epics.get(subtask.getEpicIdForThisSubtask()));
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic epicLink = epics.get(epic.getId());
        epicLink.setName(epic.getName());
        epicLink.setDescription(epic.getDescription());
    }

    @Override
    public void deleteTasks() {
        for (Task task : tasks.values()) {
            historyManager.removeFromHistory(task.getId());
        }
        tasks.clear();
    }

    @Override
    public void deleteSubtasks() {
        for (Epic epic : epics.values()) {
            epic.getSubtasksForThisEpic().clear();
            changeEpicStatus(epic);
        }
        for (Subtask subtask : subtasks.values()) {
            historyManager.removeFromHistory(subtask.getId());
        }
        subtasks.clear();
    }

    @Override
    public void deleteEpics() {
        for (Subtask subtask : subtasks.values()) {
            historyManager.removeFromHistory(subtask.getId());
        }
        for (Epic epic : epics.values()) {
            historyManager.removeFromHistory(epic.getId());
        }
        subtasks.clear();
        epics.clear();
    }

    @Override
    public List<Subtask> getAllSubtasksByEpic(int id) {
        Map<Integer, Subtask> subtasksByEpic = epics.get(id).getSubtasksForThisEpic();
        return new ArrayList<>(subtasksByEpic.values());
    }

    @Override
    public int getCountId() {
        return countId;
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void changeEpicStatus(Epic epic) {
        if (epic.getSubtasksForThisEpic().isEmpty()) {
            epic.setStatus(Status.NEW);
        } else {
            int countNew = 0;
            int countDone = 0;
            Map<Integer, Subtask> forStatus = epic.getSubtasksForThisEpic();
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
