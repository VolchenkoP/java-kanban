package ru.practicum.tasksManager.service.impl;

import ru.practicum.tasksManager.model.*;
import ru.practicum.tasksManager.service.HistoryManager;
import ru.practicum.tasksManager.service.TaskManager;
import ru.practicum.tasksManager.utilities.Managers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    final Map<Integer, Task> tasks;
    final Map<Integer, Subtask> subtasks;
    final Map<Integer, Epic> epics;
    final HistoryManager historyManager;
    int countId;
    TreeSet<Task> tasksByStartTime;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
        countId = 0;
        tasksByStartTime = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    }

    @Override
    public void saveTask(Task task) {
        if (!tasks.containsValue(task)) {
            countId++;
            task.setId(countId);
            task.setTypeOfTask(TypeOfTask.TASK);
            if (!validateStartTimeForTask(task)) {
                System.out.println("Невозможно создать задачу - ее время пересекается с другой");
                return;
            }
            tasks.put(task.getId(), task);
            addToTasksByStartTimeTreeSet(task);
        } else {
            System.out.println("Такая задача уже создана");
        }
    }

    @Override
    public void saveSubtask(Subtask subtask) {
        if (!subtasks.containsValue(subtask)) {
            countId++;
            subtask.setId(countId);
            subtask.setTypeOfTask(TypeOfTask.SUBTASK);
            if (!validateStartTimeForTask(subtask)) {
                System.out.println("Невозможно создать подзадачу - ее время пересекается с другой");
                return;
            }
            epics.values().stream()
                    .filter(epic -> epic == epics.get(subtask.getEpicIdForThisSubtask()))
                    .map(epic -> {
                        epic.addSubtasksForThisEpic(subtask);
                        changeEpicStatus(epic);
                        changeEpicEndTime(epic);
                        return epic;
                    })
                    .collect(Collectors.toList());

            subtasks.put(subtask.getId(), subtask);
            addToTasksByStartTimeTreeSet(subtask);
        } else {
            System.out.println("Такая подзадача уже создана");
        }

    }

    @Override
    public void saveEpic(Epic epic) {
        if (!epics.containsValue(epic)) {
            countId++;
            epic.setId(countId);
            epic.setTypeOfTask(TypeOfTask.EPIC);
            changeEpicEndTime(epic);
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
    public Optional<Task> getTaskById(int id) {
        if (tasks.containsKey(id)) {
            historyManager.addToHistory(tasks.get(id));
            return Optional.of(tasks.get(id));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Subtask> getSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            historyManager.addToHistory(subtasks.get(id));
            return Optional.of(subtasks.get(id));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Epic> getEpicById(int id) {
        if (epics.containsKey(id)) {
            historyManager.addToHistory(epics.get(id));
            return Optional.of(epics.get(id));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (epics.containsKey(id)) {
            epics.get(id).getSubtasksForThisEpic().keySet().forEach(someId -> {
                subtasks.remove(someId);
                historyManager.removeFromHistory(someId);
            });
            epics.remove(id);
        } else if (subtasks.containsKey(id)) {
            subtasks.values().stream()
                    .filter(subtask -> subtask.equals(subtasks.get(id)))
                    .map(subtask -> {
                        epics.get(subtask.getEpicIdForThisSubtask()).deleteSubtaskForThisEpic(id);
                        changeEpicStatus(epics.get(subtask.getEpicIdForThisSubtask()));
                        changeEpicEndTime(epics.get(subtask.getEpicIdForThisSubtask()));
                        return subtask;
                    })
                    .collect(Collectors.toList());

            subtasks.remove(id);
        }
        historyManager.removeFromHistory(id);
    }

    @Override
    public void updateTask(Task task) {
        tasks.values().stream()
                .filter(someTask -> someTask.equals(task))
                .map(someTask -> {
                    someTask.setName(task.getName());
                    someTask.setDescription(task.getDescription());
                    someTask.setStatus(task.getStatus());
                    return someTask;
                })
                .collect(Collectors.toList());

    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.values().stream()
                .filter(someSubtask -> someSubtask.equals(subtask))
                .map(someSubtask -> {
                    someSubtask.setName(subtask.getName());
                    someSubtask.setDescription(subtask.getDescription());
                    someSubtask.setStatus(subtask.getStatus());
                    changeEpicStatus(epics.get(someSubtask.getEpicIdForThisSubtask()));
                    changeEpicEndTime(epics.get(someSubtask.getEpicIdForThisSubtask()));
                    return someSubtask;
                })
                .collect(Collectors.toList());

    }

    @Override
    public void updateEpic(Epic epic) {
        epics.values().stream()
                .filter(someEpic -> someEpic.equals(epic))
                .map(someEpic -> {
                    someEpic.setName(epic.getName());
                    someEpic.setDescription(epic.getDescription());
                    return someEpic;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTasks() {
        tasks.values().forEach(task -> historyManager.removeFromHistory(task.getId()));
        tasks.clear();
    }

    @Override
    public void deleteSubtasks() {
        epics.values().forEach(epic -> {
            epic.getSubtasksForThisEpic().clear();
            changeEpicStatus(epic);
            changeEpicEndTime(epic);
        });

        subtasks.values().forEach(subtask -> historyManager.removeFromHistory(subtask.getId()));
        subtasks.clear();
    }

    @Override
    public void deleteEpics() {
        subtasks.values().forEach(subtask -> historyManager.removeFromHistory(subtask.getId()));
        epics.values().forEach(epic -> historyManager.removeFromHistory(epic.getId()));
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

    public Set<Task> getPrioritizedTasks() {
        return tasksByStartTime;
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    boolean checkTimeCrossing(Task task1, Task task2) {
        if (task1.getStartTime() == null || task2.getStartTime() == null)
            return false;
        return (task1.getEndTime().isAfter(task2.getStartTime())
                && task1.getEndTime().isBefore(task2.getEndTime()))
                || (task1.getStartTime().isAfter(task2.getStartTime())
                && task1.getStartTime().isBefore(task2.getEndTime()))
                || (task2.getEndTime().isAfter(task1.getStartTime())
                && task2.getEndTime().isBefore(task1.getEndTime()))
                || (task2.getStartTime().isAfter(task1.getStartTime())
                && task2.getStartTime().isBefore(task1.getEndTime()));


    }

    void addToTasksByStartTimeTreeSet(Task task) {
        if (task.getStartTime() != null) {
            tasksByStartTime.add(task);
        }
    }

    void changeEpicStatus(Epic epic) {
        if (epic.getSubtasksForThisEpic().isEmpty()) {
            epic.setStatus(Status.NEW);
        } else {
            ArrayList<Status> statusesFromEpicSubtasks = new ArrayList<>();
            epic.getSubtasksForThisEpic().values().forEach(
                    subtask -> statusesFromEpicSubtasks.add(subtask.getStatus()));

            if (statusesFromEpicSubtasks.stream()
                    .anyMatch(status -> status == Status.IN_PROGRESS)) {
                epic.setStatus(Status.IN_PROGRESS);
                return;
            } else if (statusesFromEpicSubtasks.stream()
                    .anyMatch(status -> status == Status.NEW) &&
                    statusesFromEpicSubtasks.stream()
                            .anyMatch(status -> status == Status.DONE)) {
                epic.setStatus(Status.IN_PROGRESS);
                return;
            } else if (statusesFromEpicSubtasks.stream()
                    .anyMatch(status -> status == Status.NEW)) {
                epic.setStatus(Status.NEW);
                return;
            }
            epic.setStatus(Status.DONE);
        }
    }

    void changeEpicEndTime(Epic epic) {
        if (epic.getSubtasksForThisEpic().isEmpty()) {
            epic.setStartTime(LocalDateTime.now());
            epic.setDuration(Duration.ofMinutes(15));
            epic.setEpicEndTime(epic.getStartTime().plusMinutes(epic.getDuration().toMinutes()));
        } else {
            epic.getSubtasksForThisEpic().values().stream()
                    .filter(subtask -> subtask.getStartTime() != null)
                    .min(Comparator.comparing(Subtask::getStartTime))
                    .ifPresent(subtask -> epic.setStartTime(subtask.getStartTime()));

            epic.getSubtasksForThisEpic().values().stream()
                    .filter(subtask -> subtask.getStartTime() != null)
                    .max(Comparator.comparing(Subtask::getEndTime))
                    .ifPresent(subtask -> epic.setStartTime(subtask.getEndTime()));

            epic.setDuration(Duration.between(epic.getStartTime(), epic.getEndTime()));
        }
    }

    boolean validateStartTimeForTask(Task task) {
        TypeOfTask type = task.getTypeOfTask();
        boolean check = false;
        switch (task.getTypeOfTask()) {
            case TASK:
                check = tasks.values()
                        .stream()
                        .noneMatch(t -> task.getId() != t.getId() && checkTimeCrossing(t, task));
            case SUBTASK:
                check = subtasks.values()
                        .stream()
                        .noneMatch(s -> task.getId() != s.getId() && checkTimeCrossing(s, task));
        }
        return check;
    }

}
