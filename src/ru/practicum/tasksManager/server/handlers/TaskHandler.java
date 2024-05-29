package ru.practicum.tasksManager.server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.practicum.tasksManager.model.Task;
import ru.practicum.tasksManager.service.TaskManager;
import ru.practicum.tasksManager.utilities.Managers;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

public class TaskHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson;

    public TaskHandler(TaskManager manager) {
        this.manager = manager;
        gson = Managers.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET":
                tasksGet(exchange);
                break;
            case "POST":
                tasksPost(exchange);
                break;
            case "DELETE":
                tasksDelete(exchange);
                break;
        }
    }

    private void tasksGet(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String response;

        if (Pattern.matches("^/tasks$", path)) {

            List<Task> tasks = manager.getTasks();
            response = gson.toJson(tasks);
            sendText(exchange, response);

        } else if (Pattern.matches("^/tasks/\\d+$", path)) {
            String pathId = path.replaceFirst("/tasks/", "");
            int id = parsePathId(pathId);
            if (id > 0) {
                Task task = manager.getTaskById(id);
                if (task != null) {
                    response = gson.toJson(task);
                    sendText(exchange, response);
                }
                writeResponse(exchange, "Задачи с id = " + pathId + " нет.",
                        ResponseCode.NOT_FOUND.getCode());

            } else {
                writeResponse(exchange, "Некорректный id =  " + pathId,
                        ResponseCode.NOT_FOUND.getCode());
            }
        }
    }

    private void tasksPost(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        if (Pattern.matches("^/tasks$", path)) {

            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Task reqToAddTask = gson.fromJson(body, Task.class);
            manager.saveTask(reqToAddTask);
            if (reqToAddTask.getId() != 0) {
                System.out.println("Задача создана");
                exchange.sendResponseHeaders(ResponseCode.CREATED.getCode(), 0);
                exchange.close();

            } else {
                writeResponse(exchange, "Задача пересекается с существующими",
                        ResponseCode.NOT_ACCEPTABLE.getCode());
            }
        } else if (Pattern.matches("^/tasks/\\d+$", path)) {
            String pathId = path.replaceFirst("/tasks/", "");
            int id = parsePathId(pathId);
            if (id > 0) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Task reqToUpdTask = gson.fromJson(body, Task.class);
                manager.updateTask(reqToUpdTask);
                if (reqToUpdTask.getId() != 0) {
                    System.out.println("Задача обновлена");
                    exchange.sendResponseHeaders(ResponseCode.CREATED.getCode(), 0);
                    exchange.close();

                } else {
                    writeResponse(exchange, "Задача пересекается с существующими",
                            ResponseCode.NOT_ACCEPTABLE.getCode());
                }

            } else {
                writeResponse(exchange, "Некорректный id =  " + pathId,
                        ResponseCode.NOT_ACCEPTABLE.getCode());
            }
        }
    }

    private void tasksDelete(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        if (Pattern.matches("^/tasks$", path)) {
            manager.deleteTasks();
            System.out.println("Задачи удалены");
            exchange.sendResponseHeaders(ResponseCode.CREATED.getCode(), 0);
            exchange.close();

        } else if (Pattern.matches("^/tasks/\\d+$", path)) {
            String pathId = path.replaceFirst("/tasks/", "");
            int id = parsePathId(pathId);
            if (id > 0) {
                manager.deleteTaskById(id);
                System.out.println("Задача удалена");
                exchange.sendResponseHeaders(ResponseCode.CREATED.getCode(), 0);
                exchange.close();

            } else {
                writeResponse(exchange, "Некорректный id =  " + pathId,
                        ResponseCode.NOT_ACCEPTABLE.getCode());
            }
        }
    }

    private void sendText(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(ResponseCode.OK.getCode(), resp.length);
        exchange.getResponseBody().write(resp);
    }

    private void writeResponse(HttpExchange exchange,
                               String responseString,
                               int responseCode) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(responseCode, 0);
            os.write(responseString.getBytes(StandardCharsets.UTF_8));
        }
        exchange.close();
    }

    private int parsePathId(String pathId) {
        try {
            return Integer.parseInt(pathId);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

}