package com.nodag.app.nodes

/**
 * Базовый класс для всех нод в системе
 */
abstract class BaseNode(
    val id: String,
    var name: String,
    val type: NodeType
) {
    val inputs: MutableList<NodePort> = mutableListOf()
    val outputs: MutableList<NodePort> = mutableListOf()
    var apiKey: String = ""
    var apiEndpoint: String = ""
    
    var positionX: Float = 0f
    var positionY: Float = 0f
    
    var isEnabled: Boolean = true
    
    /**
     * Выполнить ноду с заданными входными данными
     */
    abstract suspend fun execute(inputData: Map<String, Any?>): NodeResult
    
    /**
     * Добавить входной порт
     */
    fun addInput(name: String, portType: PortType = PortType.TEXT): NodePort {
        val port = NodePort(name, portType, PortDirection.INPUT)
        inputs.add(port)
        return port
    }
    
    /**
     * Добавить выходной порт
     */
    fun addOutput(name: String, portType: PortType = PortType.TEXT): NodePort {
        val port = NodePort(name, portType, PortDirection.OUTPUT)
        outputs.add(port)
        return port
    }
    
    /**
     * Получить данные из конкретного входа
     */
    fun getInputData(portName: String, inputData: Map<String, Any?>): Any? {
        return inputData[portName]
    }
    
    /**
     * Валидация ноды перед выполнением
     */
    open fun validate(): Boolean {
        return name.isNotBlank()
    }
    
    /**
     * Кленировать ноду
     */
    abstract fun clone(): BaseNode
}

/**
 * Результат выполнения ноды
 */
data class NodeResult(
    val success: Boolean,
    val data: Map<String, Any?> = emptyMap(),
    val error: String? = null
) {
    companion object {
        fun success(data: Map<String, Any?>) = NodeResult(true, data)
        fun failure(error: String) = NodeResult(false, emptyMap(), error)
    }
}

/**
 * Тип ноды
 */
enum class NodeType {
    INPUT,          // Входная нода (файл, промпт)
    OUTPUT,         // Выходная нода (результат)
    PROCESSING,     // Обрабатывающая нода (ИИ сервисы)
    CUSTOM          // Пользовательская нода
}

/**
 * Тип порта (входа/выхода)
 */
enum class PortType {
    TEXT,
    FILE,
    IMAGE,
    NUMBER,
    BOOLEAN,
    JSON
}

/**
 * Направление порта
 */
enum class PortDirection {
    INPUT,
    OUTPUT
}

/**
 * Порт ноды
 */
data class NodePort(
    val name: String,
    val type: PortType,
    val direction: PortDirection
) {
    var isConnected: Boolean = false
    var connectedTo: String? = null // ID ноды, к которой подключен
}
