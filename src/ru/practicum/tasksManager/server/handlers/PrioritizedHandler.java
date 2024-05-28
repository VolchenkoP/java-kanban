package ru.practicum.tasksManager.server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.practicum.tasksManager.model.Task;
import ru.practicum.tasksManager.service.TaskManager;
import ru.practicum.tasksManager.utilities.Managers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

public class PrioritizedHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson;

    public PrioritizedHandler(TaskManager manager) {
        this.manager = manager;
        gson = Managers.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (method.equals("GET")) {
            String path = exchange.getRequestURI().getPath();
            if (Pattern.matches("^/prioritized$", path)) {
                List<Task> prioritized = manager.getPrioritizedTasks();
                String responseJson = gson.toJson(prioritized);
                byte[] responseByte = responseJson.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
                exchange.sendResponseHeaders(ResponseCode.OK.getCode(), responseByte.length);
                exchange.getResponseBody().write(responseByte);
            } else {
                System.out.println("Неверный путь " + path);
                exchange.sendResponseHeaders(ResponseCode.NOT_ACCEPTABLE.getCode(), 0);
            }

        } else {
            System.out.println("Неверный метод: " + method + ". Необходимо применить метод GET");
            exchange.sendResponseHeaders(ResponseCode.NOT_ACCEPTABLE.getCode(), 0);
        }

    }
}