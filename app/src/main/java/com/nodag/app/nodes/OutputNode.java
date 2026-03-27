package com.nodag.app.nodes;

import java.util.HashMap;
import java.util.Map;

/**
 * Выходная нода для отображения текста
 */
public class OutputNode extends BaseNode {
    
    private String outputText;

    public OutputNode(String id, String name) {
        super(id, name, NodeType.OUTPUT);
        this.outputText = "";
        addInput("Текст", PortType.TEXT);
    }

    public OutputNode(String id) {
        this(id, "Output");
    }

    @Override
    public NodeResult execute(Map<String, Object> inputData) {
        outputText = inputData.containsKey("Текст") 
            ? (String) inputData.get("Текст") 
            : "";
        
        Map<String, Object> result = new HashMap<>();
        result.put("output", outputText);
        return NodeResult.success(result);
    }

    @Override
    public BaseNode clone() {
        OutputNode clone = new OutputNode(id, name);
        clone.outputText = outputText;
        clone.positionX = positionX;
        clone.positionY = positionY;
        return clone;
    }

    public String getOutputText() { return outputText; }
    public void setOutputText(String outputText) { this.outputText = outputText; }
}
