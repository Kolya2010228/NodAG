package com.nodag.app.data.model

import kotlinx.serialization.Serializable

/**
 * Основная модель ноды в воркфлоу
 */
@Serializable
data class Node(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val type: NodeType,
    val serviceType: ServiceType? = null,
    val position: NodePosition,
    val config: NodeConfig,
    val inputs: List<NodePort> = emptyList(),
    val outputs: List<NodePort> = emptyList()
)

@Serializable
enum class NodeType {
    INPUT,      // Входные данные (файл, текст)
    PROCESS,    // Обработка (AI сервисы)
    OUTPUT,     // Выходные данные
    LOGIC,      // Логические операции
    UTIL        // Утилиты
}

@Serializable
enum class ServiceType {
    OPENAI,
    ANTHROPIC,
    STABLE_DIFFUSION,
    GOOGLE_AI,
    CUSTOM
}

@Serializable
data class NodePosition(
    val x: Float,
    val y: Float
)

@Serializable
data class NodeConfig(
    val apiEndpoint: String? = null,
    val apiKeyRef: String? = null,
    val parameters: Map<String, Any> = emptyMap(),
    val customData: String? = null
)

@Serializable
data class NodePort(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val dataType: DataType,
    val description: String? = null,
    val isRequired: Boolean = true,
    val defaultValue: String? = null
)

@Serializable
enum class DataType {
    STRING,
    NUMBER,
    BOOLEAN,
    FILE,
    IMAGE,
    ARRAY,
    OBJECT,
    ANY
}
