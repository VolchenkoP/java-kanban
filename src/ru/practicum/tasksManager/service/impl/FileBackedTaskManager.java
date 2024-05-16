package ru.practicum.tasksManager.service.impl;

import ru.practicum.tasksManager.exception.ManagerSaveException;
import ru.practicum.tasksManager.model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final String header;
    private final File fileToHistory;

    public FileBackedTaskManager(File file) {
        super();
        fileToHistory = file;
        header = String.format("%5s,%s,%s,%s,%s,%s,%s,%-3s\n", "ID", "Type", "Name", "Status", "Description",
                "StartTime", "Duration", "Epic");
    }

    public static FileBackedTaskManager newFileBackedTaskManager(File file) {
        return new FileBackedTaskManager(file);
    }

    public static FileBackedTaskManager loadTaskManagerFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        fileBackedTaskManager.readTasksFromFile();
        return fileBackedTaskManager;
    }

    @Override
    public void saveTask(Task task) {
        super.saveTask(task);
        save();
    }

    @Override
    public void saveSubtask(Subtask subtask) {
        super.saveSubtask(subtask);
        save();
    }

    @Override
    public void saveEpic(Epic epic) {
        super.saveEpic(epic);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    public File getFileToHistory() {
        return fileToHistory;
    }

    private void readTasksFromFile() {

        try (BufferedReader reader = new BufferedReader(new FileReader(fileToHistory, StandardCharsets.UTF_8))) {
            int maxId = 0;
            reader.readLine(); //пропускаем хедер
            while (reader.ready()) {
                String[] split = reader.readLine().split(",");
                int idFromFile = Integer.parseInt(split[0].trim());
                if (maxId < idFromFile) {
                    maxId = idFromFile;
                }
                createATask(split[0].trim(), split[1].trim(), split[2].trim(), split[3].trim(), split[4].trim(),
                        split[5].trim(), split[6].trim(), split[7]);
            }
            countId = maxId;
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла");
        }
    }

    private void createATask(String idEx, String type, String name, String status,
                             String description, String startTime, String duration, String idEpicForSub) {
        int id = Integer.parseInt(idEx);
        LocalDateTime startTimeFromFile = LocalDateTime.parse(startTime, Task.DATE_TIME_FORMATTER);
        Duration durationFromFile = Duration.parse(duration);

        switch (type) {
            case "TASK":
                Task task = new Task(name, description, getStatus(status));
                task.setId(id);
                task.setTypeOfTask(TypeOfTask.TASK);
                task.setStartTime(startTimeFromFile);
                task.setDuration(durationFromFile);
                tasks.put(id, task);
                break;
            case "EPIC":
                Epic epic = new Epic(name, description);
                epic.setId(id);
                subtasks.values().forEach(subtask -> {
                    if (subtask.getEpicIdForThisSubtask() == epic.getId()) {
                        epic.addSubtasksForThisEpic(subtask);
                    }
                });

                setEpicForChangeStatusEndTime(epic);
                epic.setTypeOfTask(TypeOfTask.EPIC);
                epics.put(id, epic);
                break;
            default:
                Subtask subtask = new Subtask(name, description);
                subtask.setStatus(getStatus(status));
                subtask.setEpicIdForThisSubtask(Integer.parseInt(idEpicForSub.trim()));
                subtask.setId(id);
                subtask.setTypeOfTask(TypeOfTask.SUBTASK);
                subtask.setStartTime(startTimeFromFile);
                subtask.setDuration(durationFromFile);
                subtasks.put(id, subtask);
                Epic epicSaved = epics.get(subtask.getEpicIdForThisSubtask());
                if (epicSaved == null) {
                    return;
                }
                epicSaved.addSubtasksForThisEpic(subtask);
                setEpicForChangeStatusEndTime(epicSaved);
        }
    }

    private void save() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileToHistory, StandardCharsets.UTF_8));
            writer.write(header);

            tasks.values().forEach(task -> {
                try {
                    writer.write(convertTaskToString(task));
                } catch (IOException e) {
                    throw new ManagerSaveException("Ошибка записи задачи в файл");
                }
            });

            epics.values().forEach(epic -> {
                try {
                    writer.write(convertTaskToString(epic));
                } catch (IOException e) {
                    throw new ManagerSaveException("Ошибка записи эпика в файл");
                }
            });

            subtasks.values().forEach(subtask -> {
                try {
                    writer.write(convertTaskToString(subtask));
                } catch (IOException e) {
                    throw new ManagerSaveException("Ошибка записи подзадачи в файл");
                }
            });

            writer.close();
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи в файл");
        }
    }

    private String convertTaskToString(Task example) {
        String idEx = String.valueOf(example.getId());
        String type = String.valueOf(example.getTypeOfTask());
        String name = example.getName();
        String status = String.valueOf(example.getStatus());
        String desc = example.getDescription();
        String startTime = example.getStartTime().format(Task.DATE_TIME_FORMATTER);
        String duration = example.getDuration().toString();
        String epicIdForSub = "";
        if (example.getTypeOfTask().equals(TypeOfTask.SUBTASK)) {
            Subtask subtask = (Subtask) example;
            epicIdForSub = String.valueOf(subtask.getEpicIdForThisSubtask());
        }
        return String.format("%5s,%s,%s,%s,%s,%s,%s,%-3s\n", idEx, type, name, status, desc, startTime, duration, epicIdForSub);
    }

    private Status getStatus(String status) {
        return Status.valueOf(status);
    }

}
