package ru.practicum.tasksManager.service.impl;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    public InMemoryTaskManager createTaskManager() {
        return taskManager = new InMemoryTaskManager();
    }
}