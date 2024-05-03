package ru.practicum.tasksManager.service.impl;

import ru.practicum.tasksManager.exception.ManagerSaveException;
import ru.practicum.tasksManager.model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final String header;
    private final File fileToHistory;

    public FileBackedTaskManager(File file) {
        super();
        fileToHistory = file;
        header = String.format("%5s,%s,%s,%s,%s,%-3s\n", "ID", "Type", "Name", "Status", "Description", "Epic");
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
    public void deleteById(int id) {
        super.deleteById(id);
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
                        split[5]);
            }
            countId = maxId;
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла");
        }
    }

    private void createATask(String idEx, String type, String name, String status,
                             String description, String idEpicForSub) {
        int id;
        switch (type) {
            case "TASK":
                Task task = new Task(name, description, getStatus(status));
                id = Integer.parseInt(idEx);
                task.setId(id);
                task.setTypeOfTask(TypeOfTask.TASK);
                tasks.put(id, task);
                break;
            case "EPIC":
                Epic epic = new Epic(name, description);
                id = Integer.parseInt(idEx);
                epic.setId(id);
                for (Subtask subtask : subtasks.values()) {
                    if (subtask.getEpicIdForThisSubtask() == epic.getId()) {
                        epic.addSubtasksForThisEpic(subtask);
                    }
                }
                changeEpicStatus(epic);
                epic.setTypeOfTask(TypeOfTask.EPIC);
                epics.put(id, epic);
                break;
            default:
                Subtask subtask = new Subtask(name, description);
                subtask.setStatus(getStatus(status));
                subtask.setEpicIdForThisSubtask(Integer.parseInt(idEpicForSub.trim()));
                id = Integer.parseInt(idEx);
                subtask.setId(id);
                subtask.setTypeOfTask(TypeOfTask.SUBTASK);
                subtasks.put(id, subtask);
                Epic epicSaved = epics.get(subtask.getEpicIdForThisSubtask());
                if (epicSaved == null) {
                    return;
                }
                epicSaved.addSubtasksForThisEpic(subtask);
                changeEpicStatus(epicSaved);
        }
    }

    private void save() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileToHistory, StandardCharsets.UTF_8));
            writer.write(header);

            for (Task task : tasks.values()) {
                writer.write(convertTaskToString(task));
            }
            for (Epic epic : epics.values()) {
                writer.write(convertTaskToString(epic));
            }
            for (Subtask subtask : subtasks.values()) {
                writer.write(convertTaskToString(subtask));
            }
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
        String epicIdForSub = "";
        if (example.getTypeOfTask().equals(TypeOfTask.SUBTASK)) {
            Subtask subtask = (Subtask) example;
            epicIdForSub = String.valueOf(subtask.getEpicIdForThisSubtask());
        }
        return String.format("%5s,%s,%s,%s,%s,%-3s\n", idEx, type, name, status, desc, epicIdForSub);
    }

    private Status getStatus(String status) {
        return Status.valueOf(status);
    }

}
