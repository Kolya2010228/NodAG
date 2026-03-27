package com.nodag.app.nodes

import java.io.File

/**
 * Входная нода для ввода промпта (текста)
 */
class InputPromptNode(id: String, name: String = "Input Prompt") : BaseNode(id, name, NodeType.INPUT) {
    
    var defaultPrompt: String = ""
    
    init {
        addOutput("Текст", PortType.TEXT)
    }
    
    override suspend fun execute(inputData: Map<String, Any?>): NodeResult {
        val prompt = inputData["Текст"] as? String ?: defaultPrompt
        return if (prompt.isNotBlank()) {
            NodeResult.success(mapOf("Текст" to prompt))
        } else {
            NodeResult.failure("Промпт не может быть пустым")
        }
    }
    
    override fun validate(): Boolean {
        return super.validate()
    }
    
    override fun clone(): BaseNode {
        val clone = InputPromptNode(id, name)
        clone.defaultPrompt = defaultPrompt
        clone.positionX = positionX
        clone.positionY = positionY
        clone.apiKey = apiKey
        return clone
    }
}

/**
 * Входная нода для загрузки файла
 */
class InputFileNode(id: String, name: String = "Input File") : BaseNode(id, name, NodeType.INPUT) {
    
    var selectedFile: File? = null
    
    init {
        addOutput("Данные", PortType.FILE)
        addOutput("Путь", PortType.TEXT)
    }
    
    override suspend fun execute(inputData: Map<String, Any?>): NodeResult {
        val file = selectedFile
        return if (file != null && file.exists()) {
            NodeResult.success(
                mapOf(
                    "Данные" to file.readBytes(),
                    "Путь" to file.absolutePath
                )
            )
        } else {
            NodeResult.failure("Файл не выбран или не существует")
        }
    }
    
    override fun clone(): BaseNode {
        val clone = InputFileNode(id, name)
        clone.selectedFile = selectedFile
        clone.positionX = positionX
        clone.positionY = positionY
        return clone
    }
}

/**
 * Выходная нода для отображения текста
 */
class OutputNode(id: String, name: String = "Output") : BaseNode(id, name, NodeType.OUTPUT) {
    
    var outputText: String = ""
    
    init {
        addInput("Текст", PortType.TEXT)
    }
    
    override suspend fun execute(inputData: Map<String, Any?>): NodeResult {
        outputText = inputData["Текст"] as? String ?: ""
        return NodeResult.success(mapOf("output" to outputText))
    }
    
    override fun clone(): BaseNode {
        val clone = OutputNode(id, name)
        clone.outputText = outputText
        clone.positionX = positionX
        clone.positionY = positionY
        return clone
    }
}
