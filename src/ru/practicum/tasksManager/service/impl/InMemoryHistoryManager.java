package ru.practicum.tasksManager.service.impl;

import ru.practicum.tasksManager.model.Node;
import ru.practicum.tasksManager.model.Task;
import ru.practicum.tasksManager.service.HistoryManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> requestHistory = new HashMap();

    private Node head;
    private Node tail;

    private Node newNode(Task task){
        if (head == null){
            head = new Node(null, task, null);
            return head;
        } else if (tail == null){
            tail = new Node(head, task,null);
            head.setNext(tail);
            return tail;
        } else {
            final Node prevNode = tail;
            Node node = new Node(prevNode, task, null);
            prevNode.setNext(node);
            tail = node;
            return node;
        }
    }

    private void removeNode(Node node){
        if (node == null){
            return;
        } else {
            Node prev = node.getPrev();
            Node next = node.getNext();

            if (node == head){
                head = next;
            }

            if (node == tail){
                tail = prev;
            }

            if (prev != null) {
                prev.setNext(node.getNext());
            }

            if (next != null) {
                next.setPrev(node.getPrev());
            }
        }
    }

    @Override
    public void addToHistory(Task task) {
            int taskId = task.getId();
            if (requestHistory.containsKey(taskId)){
                removeNode(requestHistory.get(taskId));
                requestHistory.remove(taskId);
            }
            requestHistory.put(taskId, newNode(task));
    }

    @Override
    public void removeFromHistory(int id){
        Node node = requestHistory.get(id);
        requestHistory.remove(id);
        removeNode(node);
    }

    @Override
    public List<Task> getHistory() {
        List<Task> history = new ArrayList<>();
        Node node = head;
        while (node != null){
            history.add(node.getTask());
            node = node.getNext();
        }
        return history;
    }
}
