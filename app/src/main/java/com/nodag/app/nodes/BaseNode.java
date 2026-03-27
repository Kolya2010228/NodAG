package com.nodag.app.nodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Базовый класс для всех нод в системе
 */
public abstract class BaseNode {
    protected String id;
    protected String name;
    protected NodeType type;
    
    protected List<NodePort> inputs;
    protected List<NodePort> outputs;
    protected String apiKey;
    protected String apiEndpoint;
    
    protected float positionX;
    protected float positionY;
    
    protected boolean isEnabled;

    public BaseNode(String id, String name, NodeType type) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.inputs = new ArrayList<>();
        this.outputs = new ArrayList<>();
        this.apiKey = "";
        this.apiEndpoint = "";
        this.positionX = 0f;
        this.positionY = 0f;
        this.isEnabled = true;
    }

    /**
     * Выполнить ноду с заданными входными данными
     */
    public abstract NodeResult execute(Map<String, Object> inputData);

    /**
     * Добавить входной порт
     */
    public NodePort addInput(String name, PortType portType) {
        NodePort port = new NodePort(name, portType, PortDirection.INPUT);
        inputs.add(port);
        return port;
    }

    /**
     * Добавить выходной порт
     */
    public NodePort addOutput(String name, PortType portType) {
        NodePort port = new NodePort(name, portType, PortDirection.OUTPUT);
        outputs.add(port);
        return port;
    }

    /**
     * Получить данные из конкретного входа
     */
    public Object getInputData(String portName, Map<String, Object> inputData) {
        return inputData.get(portName);
    }

    /**
     * Валидация ноды перед выполнением
     */
    public boolean validate() {
        return name != null && !name.isBlank();
    }

    /**
     * Клонировать ноду
     */
    public abstract BaseNode clone();

    // Геттеры и сеттеры
    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public NodeType getType() { return type; }
    public List<NodePort> getInputs() { return inputs; }
    public List<NodePort> getOutputs() { return outputs; }
    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
    public String getApiEndpoint() { return apiEndpoint; }
    public void setApiEndpoint(String apiEndpoint) { this.apiEndpoint = apiEndpoint; }
    public float getPositionX() { return positionX; }
    public void setPositionX(float positionX) { this.positionX = positionX; }
    public float getPositionY() { return positionY; }
    public void setPositionY(float positionY) { this.positionY = positionY; }
    public boolean isEnabled() { return isEnabled; }
    public void setEnabled(boolean enabled) { isEnabled = enabled; }
}
