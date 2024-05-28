package ru.practicum.tasksManager.server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.practicum.tasksManager.model.Epic;
import ru.practicum.tasksManager.service.TaskManager;
import ru.practicum.tasksManager.utilities.Managers;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

public class EpicHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson;

    public EpicHandler(TaskManager manager) {
        this.manager = manager;
        gson = Managers.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET":
                epicsGet(exchange);
                break;
            case "POST":
                epicsPost(exchange);
                break;
            case "DELETE":
                epicsDelete(exchange);
                break;
        }
    }

    private void epicsGet(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String response;

        if (Pattern.matches("^/epics$", path)) {

            List<Epic> epics = manager.getEpics();
            response = gson.toJson(epics);
            sendText(exchange, response);


        } else if (Pattern.matches("^/epics/\\d+$", path)) {
            String pathId = path.replaceFirst("/epics/", "");
            int id = parsePathId(pathId);
            if (id > 0) {
                Epic epic = manager.getEpicById(id);
                if (epic != null) {
                    response = gson.toJson(epic);
                    sendText(exchange, response);
                }
                writeResponse(exchange, "Эпика с id = " + pathId + " нет.",
                        ResponseCode.NOT_FOUND.getCode());

            } else {
                writeResponse(exchange, "Некорректный id =  " + pathId,
                        ResponseCode.NOT_ACCEPTABLE.getCode());

            }
        }
    }

    private void epicsPost(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (Pattern.matches("^/epics$", path)) {

            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Epic reqToEpic = gson.fromJson(body, Epic.class);
            manager.saveEpic(reqToEpic);
            Epic addedEpic = reqToEpic;
            if (addedEpic != null) {
                System.out.println("Эпик создан");
                exchange.sendResponseHeaders(ResponseCode.CREATED.getCode(), 0);
                exchange.close();

            } else {
                writeResponse(exchange, "Эпик пересекается с существующим",
                        ResponseCode.NOT_ACCEPTABLE.getCode());
            }
        } else if (Pattern.matches("^/epics/\\d+$", path)) {
            String pathId = path.replaceFirst("/epics/", "");
            int id = parsePathId(pathId);
            if (id > 0) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Epic reqToUpdEpic = gson.fromJson(body, Epic.class);
                manager.updateEpic(reqToUpdEpic);
                System.out.println("Эпик обновлен");
                exchange.sendResponseHeaders(ResponseCode.CREATED.getCode(), 0);
                exchange.close();

            } else {
                writeResponse(exchange, "Некорректный id =  " + pathId,
                        ResponseCode.NOT_ACCEPTABLE.getCode());

            }
        }
    }

    private void epicsDelete(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        if (Pattern.matches("^/epics$", path)) {
            manager.deleteEpics();
            System.out.println("Все эпики удалены");
            exchange.sendResponseHeaders(ResponseCode.CREATED.getCode(), 0);
            exchange.close();

        } else if (Pattern.matches("^/epics/\\d+$", path)) { //Если есть id возвращаем задачу по id
            String pathId = path.replaceFirst("/epics/", "");// должен вернуть id
            int id = parsePathId(pathId);
            if (id > 0) {
                manager.deleteEpicById(id);
                System.out.println("Эпик удален");
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