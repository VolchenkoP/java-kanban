package ru.practicum.tasksManager.service.impl;

import ru.practicum.tasksManager.exeption.ManagerSaveException;
import ru.practicum.tasksManager.model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final String header;
    private final File fileToHistory;
    private final List<Task> allTasks;

    public FileBackedTaskManager(File file) {
        super();
        fileToHistory = file;
        allTasks = new ArrayList<>();
        header = String.format("%5s,%s,%s,%s,%s,%-3s\n", "ID", "Type", "Name", "Status", "Description", "Epic");
        upLoad();
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

    private void upLoad() {
        if (getCountId() == 0) {
            readWordsFromFile();
        }
    }

    private void readWordsFromFile() {

        try (BufferedReader reader = new BufferedReader(new FileReader(fileToHistory, StandardCharsets.UTF_8))) {
            reader.readLine(); //пропускаем хедер
            while (reader.ready()) {
                String[] split = reader.readLine().split(",");
                createATask(split[1].trim(), split[2].trim(), split[3].trim(), split[4].trim(), split[5]);
            }
        } catch (ManagerSaveException e) {
            e.getMessage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createATask(String type, String name, String status, String description, String idEpicForSub) {

        switch (type) {
            case "TASK":
                Task task = new Task(name, description, checkStatus(status));
                saveTask(task);
                break;
            case "EPIC":
                Epic epic = new Epic(name, description);
                saveEpic(epic);
                break;
            default:
                Subtask subtask = new Subtask(name, description);
                subtask.setStatus(checkStatus(status));
                subtask.setEpicIdForThisSubtask(Integer.parseInt(idEpicForSub.trim()));
                saveSubtask(subtask);
        }
    }

    private void save() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileToHistory));
            writer.write(header);
            writer.close();
        } catch (ManagerSaveException e) {
            e.getMessage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        allTasks.clear();
        allTasks.addAll(this.getTasks());
        allTasks.addAll(this.getEpics());
        allTasks.addAll(this.getSubtasks());

        for (Task task : allTasks) {
            try {
                writeToFile(task);
            } catch (ManagerSaveException e) {
                e.getMessage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void writeToFile(Task example) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileToHistory, true));
        String idEx = String.valueOf(example.getId());
        String type;
        if (getEpics().contains(example)) {
            type = String.valueOf(TypeOfTask.EPIC);
        } else if (getSubtasks().contains(example)) {
            type = String.valueOf(TypeOfTask.SUBTASK);
        } else {
            type = String.valueOf(TypeOfTask.TASK);
        }
        String name = example.getName();
        String status = String.valueOf(example.getStatus());
        String desc = example.getDescription();
        String epicIdForSub = "";
        if (getSubtasks().contains(example)) {
            Subtask subtask = (Subtask) example;
            epicIdForSub = String.valueOf(subtask.getEpicIdForThisSubtask());
        }

        String line = String.format("%5s,%s,%s,%s,%s,%-3s\n", idEx, type, name, status, desc, epicIdForSub);
        writer.write(line);
        writer.close();
    }

    private Status checkStatus(String status) {
        return switch (status) {
            case "NEW" -> Status.NEW;
            case "IN_PROGRESS" -> Status.IN_PROGRESS;
            default -> Status.DONE;
        };
    }

}
