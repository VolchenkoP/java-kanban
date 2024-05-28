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

public class HistoryHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson;

    public HistoryHandler(TaskManager manager) {
        this.manager = manager;
        gson = Managers.getGson();

    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (method.equals("GET")) {
            String path = exchange.getRequestURI().getPath();
            if (Pattern.matches("^/history$", path)) {

                List<Task> history = manager.getHistory();
                String responseJson = gson.toJson(history);
                byte[] responseByte = responseJson.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
                exchange.sendResponseHeaders(ResponseCode.OK.getCode(), responseByte.length);
                exchange.getResponseBody().write(responseByte);
            } else {
                writeResponse(exchange, "Неверный путь " + path,
                        ResponseCode.NOT_ACCEPTABLE.getCode());
            }

        } else {
            writeResponse(exchange, "Неверный метод: " + method + ". Нужен метод GET",
                    ResponseCode.NOT_ACCEPTABLE.getCode());
        }
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
}