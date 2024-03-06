

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = new TaskManager();
        Task task1 = taskManager.createTask("Задача 1", "Убраться в комнате", Status.NEW);
        Task task2 = taskManager.createTask("Задача 2", "Убраться на кухне", Status.NEW);

        taskManager.saveTask(task1);
        taskManager.saveTask(task2);

        Epic epic1 = taskManager.createEpic("Эпик 1", "Вынести мусор", Status.NEW);
        Subtask subtask1 = taskManager.createSubtask("Подзадача 1", "Разделить мусор по принадлежности", Status.NEW);
        Subtask subtask2 = taskManager.createSubtask("Подзадача 2", "Вынести пакеты на мусорку", Status.NEW);

        taskManager.setSubtasksByEpic(epic1, subtask1);
        taskManager.setSubtasksByEpic(epic1, subtask2);

        taskManager.saveEpic(epic1);
        taskManager.saveSubtask(subtask1);
        taskManager.saveSubtask(subtask2);

        Epic epic2 = taskManager.createEpic("Эпик 2", "Проветрить в комнате", Status.NEW);
        Subtask subtask3 = taskManager.createSubtask("Подзадача 3", "Убраться в комнате", Status.NEW);

        taskManager.setSubtasksByEpic(epic2, subtask3);

        taskManager.saveEpic(epic2);
        taskManager.saveSubtask(subtask3);

        System.out.println(taskManager.getTasksByType("Задачи"));
        System.out.println(taskManager.getTasksByType("Эпики"));
        System.out.println(taskManager.getTasksByType("Подзадачи"));

        System.out.println("Создали.");

        taskManager.updateTask(task1, "Задача 1", "Описание 1", Status.IN_PROGRESS);
        taskManager.updateTask(task2, "Задача 2", "Описание 2", Status.IN_PROGRESS);

        taskManager.updateSubtask(subtask1, "Подзадача 1", "Описание 1", Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask2, "Подзадача 2", "Описание 2", Status.DONE);
        taskManager.updateSubtask(subtask3, "Подзадача 3", "Описание 3", Status.DONE);

        System.out.println(taskManager.getTasksByType("Задачи"));
        System.out.println(taskManager.getTasksByType("Эпики"));
        System.out.println(taskManager.getTasksByType("Подзадачи"));

        System.out.println("Обновили.");

        taskManager.deleteById(task1.getId());
        taskManager.deleteById(epic2.getId());
        taskManager.deleteById(subtask1.getId());

        System.out.println(taskManager.getTasksByType("Задачи"));
        System.out.println(taskManager.getTasksByType("Эпики"));
        System.out.println(taskManager.getTasksByType("Подзадачи"));

        System.out.println("Удалили.");

    }

}
