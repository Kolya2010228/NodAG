package com.nodag.app.nodes;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * Входная нода для загрузки файла
 */
public class InputFileNode extends BaseNode {
    
    private File selectedFile;

    public InputFileNode(String id, String name) {
        super(id, name, NodeType.INPUT);
        this.selectedFile = null;
        addOutput("Данные", PortType.FILE);
        addOutput("Путь", PortType.TEXT);
    }

    public InputFileNode(String id) {
        this(id, "Input File");
    }

    @Override
    public NodeResult execute(Map<String, Object> inputData) {
        if (selectedFile != null && selectedFile.exists()) {
            try {
                Map<String, Object> result = new HashMap<>();
                result.put("Данные", Files.readAllBytes(selectedFile.toPath()));
                result.put("Путь", selectedFile.getAbsolutePath());
                return NodeResult.success(result);
            } catch (Exception e) {
                return NodeResult.failure("Ошибка чтения файла: " + e.getMessage());
            }
        } else {
            return NodeResult.failure("Файл не выбран или не существует");
        }
    }

    @Override
    public BaseNode clone() {
        InputFileNode clone = new InputFileNode(id, name);
        clone.selectedFile = selectedFile;
        clone.positionX = positionX;
        clone.positionY = positionY;
        return clone;
    }

    public File getSelectedFile() { return selectedFile; }
    public void setSelectedFile(File selectedFile) { this.selectedFile = selectedFile; }
}
