package com.nodag.app.nodes;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * Входная нода для ввода промпта (текста)
 */
public class InputPromptNode extends BaseNode {
    
    private String defaultPrompt;

    public InputPromptNode(String id, String name) {
        super(id, name, NodeType.INPUT);
        this.defaultPrompt = "";
        addOutput("Текст", PortType.TEXT);
    }

    public InputPromptNode(String id) {
        this(id, "Input Prompt");
    }

    @Override
    public NodeResult execute(Map<String, Object> inputData) {
        String prompt = inputData.containsKey("Текст") 
            ? (String) inputData.get("Текст") 
            : defaultPrompt;
        
        if (prompt != null && !prompt.isBlank()) {
            Map<String, Object> result = new HashMap<>();
            result.put("Текст", prompt);
            return NodeResult.success(result);
        } else {
            return NodeResult.failure("Промпт не может быть пустым");
        }
    }

    @Override
    public boolean validate() {
        return super.validate();
    }

    @Override
    public BaseNode clone() {
        InputPromptNode clone = new InputPromptNode(id, name);
        clone.defaultPrompt = defaultPrompt;
        clone.positionX = positionX;
        clone.positionY = positionY;
        clone.apiKey = apiKey;
        return clone;
    }

    public String getDefaultPrompt() { return defaultPrompt; }
    public void setDefaultPrompt(String defaultPrompt) { this.defaultPrompt = defaultPrompt; }
}
