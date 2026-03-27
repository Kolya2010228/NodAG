package com.nodag.app.workflow;

import com.nodag.app.nodes.BaseNode;
import com.nodag.app.nodes.NodeResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Менеджер воркфлоу - управляет нодами и их выполнением
 */
public class WorkflowManager {

    private List<BaseNode> nodes;
    private List<Connection> connections;
    private String name;
    private String description;

    public WorkflowManager() {
        this.nodes = new ArrayList<>();
        this.connections = new ArrayList<>();
        this.name = "Untitled Workflow";
        this.description = "";
    }

    /**
     * Добавить ноду в воркфлоу
     */
    public void addNode(BaseNode node) {
        nodes.add(node);
    }

    /**
     * Удалить ноду из воркфлоу
     */
    public void removeNode(String nodeId) {
        nodes.removeIf(node -> node.getId().equals(nodeId));
        connections.removeIf(conn -> 
            conn.getFromNodeId().equals(nodeId) || conn.getToNodeId().equals(nodeId));
    }

    /**
     * Создать соединение между нодами
     */
    public void connectNodes(String fromNodeId, String fromPort, 
                             String toNodeId, String toPort) {
        Connection connection = new Connection(
            generateConnectionId(),
            fromNodeId,
            fromPort,
            toNodeId,
            toPort
        );
        connections.add(connection);
    }

    /**
     * Удалить соединение
     */
    public void removeConnection(String connectionId) {
        connections.removeIf(conn -> conn.getId().equals(connectionId));
    }

    /**
     * Выполнить весь воркфлоу
     */
    public CompletableFuture<WorkflowResult> execute() {
        return CompletableFuture.supplyAsync(() -> {
            Map<String, NodeResult> results = new HashMap<>();
            Set<String> executedNodes = new HashSet<>();

            try {
                // Находим входные ноды (без входящих соединений)
                List<BaseNode> inputNodes = nodes.stream()
                    .filter(node -> connections.stream()
                        .noneMatch(conn -> conn.getToNodeId().equals(node.getId())))
                    .collect(Collectors.toList());

                if (inputNodes.isEmpty()) {
                    return new WorkflowResult(
                        false,
                        new HashMap<>(),
                        new ArrayList<>(),
                        "Нет входных нод для запуска"
                    );
                }

                // Выполняем ноды в порядке зависимостей
                for (BaseNode node : inputNodes) {
                    executeNodeWithDependencies(node, results, executedNodes);
                }

                // Проверяем, все ли ноды выполнены
                List<String> failedNodes = results.entrySet().stream()
                    .filter(entry -> !entry.getValue().isSuccess())
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

                return new WorkflowResult(
                    failedNodes.isEmpty(),
                    results,
                    failedNodes,
                    null
                );
            } catch (Exception e) {
                return new WorkflowResult(
                    false,
                    new HashMap<>(),
                    new ArrayList<>(),
                    "Ошибка выполнения: " + e.getMessage()
                );
            }
        });
    }

    private void executeNodeWithDependencies(BaseNode node, 
                                              Map<String, NodeResult> results,
                                              Set<String> executedNodes) {
        // Если уже выполнена, пропускаем
        if (executedNodes.contains(node.getId())) return;

        // Сначала выполняем зависимости
        List<Connection> dependencies = connections.stream()
            .filter(conn -> conn.getToNodeId().equals(node.getId()))
            .collect(Collectors.toList());

        Map<String, Object> inputData = new HashMap<>();

        for (Connection connection : dependencies) {
            BaseNode fromNode = nodes.stream()
                .filter(n -> n.getId().equals(connection.getFromNodeId()))
                .findFirst()
                .orElse(null);

            if (fromNode != null) {
                // Рекурсивно выполняем зависимую ноду
                executeNodeWithDependencies(fromNode, results, executedNodes);

                // Получаем результат из предыдущей ноды
                NodeResult fromResult = results.get(connection.getFromNodeId());
                if (fromResult != null && fromResult.isSuccess()) {
                    inputData.put(connection.getToPort(), 
                        fromResult.getData().get(connection.getFromPort()));
                }
            }
        }

        // Добавляем данные из входов ноды
        node.getInputs().forEach(input -> {
            if (!inputData.containsKey(input.getName())) {
                inputData.put(input.getName(), null);
            }
        });

        // Выполняем ноду
        NodeResult result = node.execute(inputData);
        results.put(node.getId(), result);
        executedNodes.add(node.getId());
    }

    /**
     * Экспорт воркфлоу в JSON
     */
    public String exportToJson() {
        StringBuilder nodesJson = new StringBuilder();
        for (int i = 0; i < nodes.size(); i++) {
            BaseNode node = nodes.get(i);
            nodesJson.append("{")
                .append("\"id\": \"").append(node.getId()).append("\",")
                .append("\"name\": \"").append(node.getName()).append("\",")
                .append("\"type\": \"").append(node.getType()).append("\",")
                .append("\"x\": ").append(node.getPositionX()).append(",")
                .append("\"y\": ").append(node.getPositionY()).append(",")
                .append("\"apiKey\": \"").append(node.getApiKey()).append("\",")
                .append("\"apiEndpoint\": \"").append(node.getApiEndpoint()).append("\"")
                .append("}");
            if (i < nodes.size() - 1) nodesJson.append(",\n");
        }

        StringBuilder connectionsJson = new StringBuilder();
        for (int i = 0; i < connections.size(); i++) {
            Connection conn = connections.get(i);
            connectionsJson.append("{")
                .append("\"id\": \"").append(conn.getId()).append("\",")
                .append("\"from\": \"").append(conn.getFromNodeId()).append("\",")
                .append("\"to\": \"").append(conn.getToNodeId()).append("\",")
                .append("\"fromPort\": \"").append(conn.getFromPort()).append("\",")
                .append("\"toPort\": \"").append(conn.getToPort()).append("\"")
                .append("}");
            if (i < connections.size() - 1) connectionsJson.append(",\n");
        }

        return "{\n" +
            "\"name\": \"" + name + "\",\n" +
            "\"description\": \"" + description + "\",\n" +
            "\"nodes\": [\n" + nodesJson + "\n],\n" +
            "\"connections\": [\n" + connectionsJson + "\n]\n" +
            "}";
    }

    /**
     * Очистить воркфлоу
     */
    public void clear() {
        nodes.clear();
        connections.clear();
    }

    private String generateConnectionId() {
        return "conn_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }

    // Геттеры и сеттеры
    public List<BaseNode> getNodes() { return nodes; }
    public List<Connection> getConnections() { return connections; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
