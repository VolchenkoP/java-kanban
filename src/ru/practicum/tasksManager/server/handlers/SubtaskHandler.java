package ru.practicum.tasksManager.server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.practicum.tasksManager.model.Subtask;
import ru.practicum.tasksManager.service.TaskManager;
import ru.practicum.tasksManager.utilities.Managers;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;


public class SubtaskHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson;

    public SubtaskHandler(TaskManager manager) {
        this.manager = manager;
        gson = Managers.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET":
                subTasksGet(exchange);
                break;
            case "POST":
                subTasksPost(exchange);
                break;
            case "DELETE":
                subTasksDelete(exchange);
                break;
        }
    }

    private void subTasksGet(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String response;

        if (Pattern.matches("^/subtasks$", path)) {
            List<Subtask> subTasks = manager.getSubtasks();
            response = gson.toJson(subTasks);
            sendText(exchange, response);
        } else if (Pattern.matches("^/subtasks/\\d+$", path)) {
            String pathId = path.replaceFirst("/subtasks/", "");
            int id = parsePathId(pathId);
            if (id > 0) {
                Subtask subTask = manager.getSubtaskById(id);
                if (subTask != null) {
                    Subtask gettedSubTask = new Subtask(subTask.getId(), subTask.getName(),
                            subTask.getDescription(), subTask.getStatus(),
                            subTask.getStartTime(), subTask.getDuration(), subTask.getEpicIdForThisSubtask());
                    response = gson.toJson(gettedSubTask);
                    sendText(exchange, response);
                }
                writeResponse(exchange, "Подзадача с id = " + pathId + " не найдена.",
                        ResponseCode.NOT_FOUND.getCode());

            } else {
                writeResponse(exchange, "Некорректный id =  " + pathId,
                        ResponseCode.NOT_ACCEPTABLE.getCode());
            }
        }
    }

    private void subTasksPost(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (Pattern.matches("^/subtasks$", path)) {

            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Subtask reqToAddSubTask = gson.fromJson(body, Subtask.class);
            manager.saveSubtask(reqToAddSubTask);
            if (reqToAddSubTask.getId() != 0) {
                System.out.println("Подзадача создана");
                exchange.sendResponseHeaders(ResponseCode.CREATED.getCode(), 0);
                exchange.close();

            } else {
                writeResponse(exchange, "Подзадача пересекается с существующими",
                        ResponseCode.NOT_ACCEPTABLE.getCode());
            }
        } else if (Pattern.matches("^/subtasks/\\d+$", path)) {
            String pathId = path.replaceFirst("/subtasks/", "");
            int id = parsePathId(pathId);
            if (id > 0) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Subtask subTaskRequest = gson.fromJson(body, Subtask.class);
                manager.updateSubtask(subTaskRequest);
                if (subTaskRequest.getId() != 0) {
                    System.out.println("Подзадача обновлена");
                    exchange.sendResponseHeaders(ResponseCode.CREATED.getCode(), 0);
                    exchange.close();

                } else {
                    writeResponse(exchange, "Подзадача пересекается с существующими",
                            ResponseCode.NOT_ACCEPTABLE.getCode());
                }

            } else {
                writeResponse(exchange, "Некорректный id =  " + pathId,
                        ResponseCode.NOT_ACCEPTABLE.getCode());
            }
        }
    }

    private void subTasksDelete(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        if (Pattern.matches("^/subtasks$", path)) {
            manager.deleteSubtasks();
            System.out.println("Все подзадачи удалены");
            exchange.sendResponseHeaders(ResponseCode.CREATED.getCode(), 0);
            exchange.close();
        } else if (Pattern.matches("^/subtasks/\\d+$", path)) {
            String pathId = path.replaceFirst("/subtasks/", "");
            int id = parsePathId(pathId);
            if (id > 0) {
                manager.deleteSubtaskById(id);
                System.out.println("Подзадача удалена");
                exchange.sendResponseHeaders(ResponseCode.CREATED.getCode(), 0);
                exchange.close();

            } else {
                writeResponse(exchange, "Некорректный id =  " + pathId + pathId,
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