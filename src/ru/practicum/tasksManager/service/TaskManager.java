package ru.practicum.tasksManager.service;

import ru.practicum.tasksManager.model.*;
import ru.practicum.tasksManager.model.Status;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int countId;
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Subtask> subtasks;
    private final HashMap<Integer, Epic> epics;

    public TaskManager() {
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
        countId = 0;
    }

    public void saveTask(Task task) {
        if (!tasks.containsValue(task)) {
            countId++;
            task.setId(countId);
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Такая задача уже создана");
        }
    }

    public void saveSubtask(Subtask subtask) {
        if (!subtasks.containsValue(subtask)) {
            countId++;
            subtask.setId(countId);
            if (subtask.getEpicIdForThisSubtask() != 0) {
                if (epics.containsKey(subtask.getEpicIdForThisSubtask())) {
                    Epic epicCopy = epics.get(subtask.getEpicIdForThisSubtask());
                    epicCopy.setSubtasksForThisEpic(subtask);
                    changeEpicStatus(epicCopy);
                }
            }
            subtasks.put(subtask.getId(), subtask);
        } else {
            System.out.println("Такая подзадача уже создана");
        }

    }

    public void saveEpic(Epic epic) {
        if (!epics.containsValue(epic)) {
            countId++;
            epic.setId(countId);
            epics.put(epic.getId(), epic);
        } else {
            System.out.println("Такой эпик уже создан");
        }
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public void deleteById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (epics.containsKey(id)) {
            HashMap<Integer, Subtask> epicSubtasksCopy = epics.get(id).getSubtasksForThisEpic();
            for (Integer num : epicSubtasksCopy.keySet()) {
                epicSubtasksCopy.get(num).deleteEpicId();
            }
            epics.remove(id);
        } else if (subtasks.containsKey(id)) {
            Subtask subtaskCopy = subtasks.get(id);
            if (subtaskCopy.getEpicIdForThisSubtask() != 0) {
                Epic epicCopy = epics.get(subtaskCopy.getEpicIdForThisSubtask());
                epicCopy.deleteSubtaskForThisEpic(id);
                changeEpicStatus(epicCopy);
            }
            subtasks.remove(id);
        }
    }

    public void updateTask(Task task) {
        Task taskCopy = tasks.get(task.getId());
        taskCopy.setName(task.getName());
        taskCopy.setDescription(task.getDescription());
        taskCopy.setStatus(task.getStatus());
    }

    public void updateSubtask(Subtask subtask) {
        Subtask subtaskCopy = subtasks.get(subtask.getId());
        subtaskCopy.setName(subtask.getName());
        subtaskCopy.setDescription(subtask.getDescription());
        subtaskCopy.setStatus(subtask.getStatus());
        changeEpicStatus(epics.get(subtask.getEpicIdForThisSubtask()));
    }

    public void updateEpic(Epic epic) {
        Epic epicCopy = epics.get(epic.getId());
        epicCopy.setName(epic.getName());
        epicCopy.setDescription(epic.getDescription());
    }

    private void changeEpicStatus(Epic epic) {
        if (epic.getSubtasksForThisEpic().isEmpty()) {
            epic.setStatus(Status.NEW);
        } else {
            int countNew = 0;
            int countProgress = 0;
            int countDone = 0;
            HashMap<Integer, Subtask> forStatus = epic.getSubtasksForThisEpic();
            for (Subtask subtaskCopy : forStatus.values()) {
                if (subtaskCopy.getStatus() == Status.IN_PROGRESS) {
                    epic.setStatus(Status.IN_PROGRESS);
                    break;
                } else if (subtaskCopy.getStatus() == Status.NEW) {
                    countNew++;
                } else if (subtaskCopy.getStatus() == Status.DONE) {
                    countDone++;
                }
            }
            if (countNew > 0 && countDone > 0) {
                epic.setStatus(Status.IN_PROGRESS);
            } else if (countNew > 0 && countDone == 0) {
                epic.setStatus(Status.NEW);
            } else if (countNew == 0 && countDone > 0) {
                epic.setStatus(Status.DONE);
            }
        }
    }

    public void deleteTasks() {
        tasks.clear();
    }

    public void deleteSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            Epic epicCopy = epics.get(subtask.getEpicIdForThisSubtask());
            epicCopy.deleteSubtaskForThisEpic(subtask.getId());
            changeEpicStatus(epicCopy);
        }
        subtasks.clear();
    }

    public void deleteEpics() {
        for (Epic epic : epics.values()) {
            HashMap<Integer, Subtask> subtasksForThisEpicCopy = epic.getSubtasksForThisEpic();
            for (Subtask subtask : subtasksForThisEpicCopy.values()) {
                subtask.deleteEpicId();
            }
        }
        epics.clear();
    }

    public ArrayList<Subtask> getAllSubtasksByEpic(int id) {
        HashMap<Integer, Subtask> subtasksByEpic = epics.get(id).getSubtasksForThisEpic();
        return new ArrayList<>(subtasksByEpic.values());
    }

}
