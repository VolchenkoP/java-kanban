package ru.practicum.tasksManager.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.tasksManager.model.*;
import ru.practicum.tasksManager.service.TaskManager;
import ru.practicum.tasksManager.utilities.Managers;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HttpTaskServerTest {
    private final URI URI_CONST = URI.create("http://localhost:8080");
    private final Gson gson = Managers.getGson();
    private HttpTaskServer taskServer;
    private TaskManager taskManager;
    private Task taskTest;
    private Epic epicTest;
    private Subtask subTaskTest;

    @BeforeEach
    public void setUp() throws IOException {
        taskTest = new Task("Модуль 2", "Завершить модуль 2", Status.NEW);
        taskTest.setStartTime(LocalDateTime.of(2024, 5, 12, 8, 00));
        taskTest.setDuration(Duration.ofMinutes(600));
        epicTest = new Epic("Окончить обучение Java",
                "Сдать следующий модуль обучения");
        epicTest.setStartTime(LocalDateTime.of(2024, 5, 12, 8, 39));
        epicTest.setDuration(Duration.ofMinutes(10));
        subTaskTest = new Subtask("Изучить и сдать 3 модуль",
                "Изучить новые темы");
        subTaskTest.setStartTime(LocalDateTime.of(2024, 5, 12, 17, 1));
        subTaskTest.setDuration(Duration.ofMinutes(600));
        subTaskTest.setEpicIdForThisSubtask(1);
        taskManager = Managers.getDefault();
        taskServer = new HttpTaskServer(taskManager);

        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    //1
    @Test
    void getTasksFromJsonShouldSuccessfullyGetResponseAfterSaveTaskTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        taskManager.saveTask(taskTest);
        URI uri = URI.create(URI_CONST + "/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> actual = gson.fromJson(response.body(), taskType);

        Assertions.assertNotNull(actual, "Список пуст");
        Assertions.assertEquals(1, actual.size(), "Неверное количество");
        Assertions.assertEquals(taskTest, actual.getFirst(), "Задачи не совпадают");
    }

    //2
    @Test
    void getTaskByIdFromJsonShouldSuccessfullyGetTaskByIdFromResponseAfterSaveTaskTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        taskManager.saveTask(taskTest);
        URI uri = URI.create(URI_CONST + "/tasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<Task>() {
        }.getType();
        Task actual = gson.fromJson(response.body(), taskType);

        Assertions.assertNotNull(actual, "Список пуст");
        Assertions.assertEquals(taskTest, actual, "Задачи не совпадают");
    }

    //3
    @Test
    void postTaskShouldSuccessfullyCreateAndPostTaskTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        Task taskPost = new Task("Модуль 2",
                "Завершить модуль 2", Status.NEW);
        taskPost.setStartTime(LocalDateTime.of(2024, 5, 12, 8, 0));
        taskPost.setDuration(Duration.ofMinutes(300));

        URI uri = URI.create(URI_CONST + "/tasks");
        String jsonTask = gson.toJson(taskPost);
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(jsonTask, StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(bodyPublisher)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
    }

    //4
    @Test
    void postUpdateTaskShouldSuccessfullySaveTaskAndUpdateTaskByPostTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        taskManager.saveTask(taskTest);

        Task taskPost = new Task("Модуль 2",
                "Завершить модуль 2", Status.NEW);
        taskPost.setId(taskTest.getId());
        taskPost.setStartTime(LocalDateTime.now());
        taskPost.setDuration(Duration.ofMinutes(300));
        taskPost.setTypeOfTask(TypeOfTask.TASK);

        taskManager.updateTask(taskPost);

        URI uri = URI.create(URI_CONST + "/tasks/1");
        String jsonTask = gson.toJson(taskPost);
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(jsonTask, StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(bodyPublisher)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
    }

    //5
    @Test
    void deleteTasksFromServerShouldSuccessfullyTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(URI_CONST + "/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
    }

    //6
    @Test
    void deleteTaskByIdFromTheServerShouldSuccessfullyTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(URI_CONST + "/tasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
    }

    //7
    @Test
    void getEpicsFromServerShouldSuccessfullySaveEpicAndGetEpicsFromServerTest() throws IOException,
            InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        taskManager.saveEpic(epicTest);
        URI uri = URI.create(URI_CONST + "/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());

        Type epicType = new TypeToken<ArrayList<Epic>>() {
        }.getType();
        List<Epic> actual = gson.fromJson(response.body(), epicType);

        Assertions.assertNotNull(actual, "Список пуст");
        Assertions.assertEquals(1, actual.size(), "Неверное количество");
        Assertions.assertEquals(epicTest, actual.get(0), "Задачи не совпадают");
    }

    //8
    @Test
    void getEpicByIdFromServerShouldSuccessfullySaveAndGetEpicFromJsonTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        taskManager.saveEpic(epicTest);
        URI uri = URI.create(URI_CONST + "/epics/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<Epic>() {
        }.getType();
        Epic actual = gson.fromJson(response.body(), taskType);

        Assertions.assertNotNull(actual, "Список пуст");
        Assertions.assertEquals(epicTest, actual, "Задачи не совпадают");
    }

    //9
    @Test
    void postEpicShouldSuccessfullyCreateAndPostEpicTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        Epic epicPost = new Epic("Окончить обучение Java",
                "Сдать следующий модуль обучения");
        epicPost.setStartTime(LocalDateTime.now());
        epicPost.setDuration(Duration.ofMinutes(0));

        URI uri = URI.create(URI_CONST + "/epics");
        String jsonEpic = gson.toJson(epicPost);
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(jsonEpic, StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(bodyPublisher)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
    }

    //10
    @Test
    void postUpdateEpicShouldSuccessfullySaveEpicAndUpdateByPostTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        Epic epicPost = new Epic("Окончить обучение Java",
                "Сдать следующий модуль обучения");
        epicPost.setStartTime(LocalDateTime.now());
        epicPost.setDuration(Duration.ofMinutes(0));

        URI uri = URI.create(URI_CONST + "/epics/1");
        String jsonEpic = gson.toJson(epicPost);
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(jsonEpic, StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(bodyPublisher)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
    }

    //11
    @Test
    void deleteEpicsFromServerShouldSuccessfullyTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(URI_CONST + "/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
    }

    //12
    @Test
    void deleteEpicByIdFromServerShouldSuccessfullyTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(URI_CONST + "/epics/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
    }

    //13
    @Test
    void getSubtasksFromServerShouldSuccessfullySaveSubtaskAndGetFromServerTest() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        taskManager.saveEpic(epicTest);
        taskManager.saveSubtask(subTaskTest);
        URI uri = URI.create(URI_CONST + "/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());

        Type subTaskType = new TypeToken<ArrayList<Subtask>>() {
        }.getType();
        List<Subtask> subTask = gson.fromJson(response.body(), subTaskType);
        List<Subtask> actual = subTask.stream()
                .map(task -> new Subtask(task.getId(), task.getName(),
                        task.getDescription(), Status.NEW, task.getStartTime(), task.getDuration(), epicTest.getId()))
                .collect(Collectors.toList());

        Assertions.assertNotNull(actual, "Список пуст");
        Assertions.assertEquals(1, actual.size(), "Неверное количество");
        Assertions.assertEquals(subTaskTest, actual.getFirst(), "Задачи не совпадают");
    }

    //14
    @Test
    void getSubtaskByIdFromServerShouldSuccessfullySaveAndGetSubtaskTest() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        taskManager.saveEpic(epicTest);
        taskManager.saveSubtask(subTaskTest);
        URI uri = URI.create(URI_CONST + "/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());

        Type subTaskType = new TypeToken<Subtask>() {
        }.getType();
        Subtask subTask = gson.fromJson(response.body(), subTaskType);
        Subtask actual = new Subtask(subTask.getId(), subTask.getName(), subTask.getDescription(), Status.NEW,
                subTask.getStartTime(), subTask.getDuration(), epicTest.getId());

        Assertions.assertNotNull(actual, "Список пуст");
        Assertions.assertEquals(subTaskTest, actual, "Задачи не совпадают");
    }

    //15
    @Test
    void postSubtaskShouldSuccessfullyCreateAndPostSubtaskTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        Subtask subTaskPost = new Subtask("Изучить и сдать 3 модуль",
                "Изучить новые темы",
                600, LocalDateTime.of(2024, 5, 12, 17, 1),
                epicTest.getId());

        URI uri = URI.create(URI_CONST + "/subtasks");
        String jsonEpic = gson.toJson(subTaskPost);
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(jsonEpic, StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(bodyPublisher)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
    }

    //16
    @Test
    void postUpdateSubtaskShouldSuccessfullySaveSubtaskAndUpdateByPostTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        taskManager.saveEpic(epicTest);
        taskManager.saveSubtask(subTaskTest);
        Subtask subTaskPost = new Subtask(2, "Изучить и сдать 3 модуль",
                "Изучить новые темы", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(600),
                epicTest.getId());
        subTaskPost.setTypeOfTask(TypeOfTask.SUBTASK);
        taskManager.updateSubtask(subTaskPost);

        URI uri = URI.create(URI_CONST + "/subtasks/1");
        String jsonEpic = gson.toJson(subTaskPost);
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(jsonEpic, StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(bodyPublisher)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
    }

    //17
    @Test
    void deleteSubtasksFromServerShouldSuccessfullyTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(URI_CONST + "/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
    }

    //18
    @Test
    void deleteSubtasksByIdFromServerShouldSuccessfullyTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(URI_CONST + "/subtasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
    }

    //19
    @Test
    void getPrioritizedTasksFromServerShouldSuccessfullyTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        taskManager.saveTask(taskTest);
        URI uri = URI.create(URI_CONST + "/prioritized");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> actual = gson.fromJson(response.body(), taskType);

        Assertions.assertNotNull(actual, "Список пуст");
        Assertions.assertEquals(1, actual.size(), "Неверное количество");
        Assertions.assertEquals(taskTest, actual.getFirst(), "Задачи не совпадают");
    }

    //20
    @Test
    void getHistoryFromServerShouldSuccessfullyTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        taskManager.saveTask(taskTest);
        taskManager.getTaskById(taskTest.getId());
        URI uri = URI.create(URI_CONST + "/history");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> actual = gson.fromJson(response.body(), taskType);

        Assertions.assertNotNull(actual, "Список пуст");
        Assertions.assertEquals(1, actual.size(), "Неверное количество");
        Assertions.assertEquals(taskTest, actual.getFirst(), "Задачи не совпадают");
    }
}