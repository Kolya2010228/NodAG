package com.nodag.app.workflow

import com.nodag.app.nodes.BaseNode
import com.nodag.app.nodes.NodeResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Менеджер воркфлоу - управляет нодами и их выполнением
 */
class WorkflowManager {
    
    val nodes: MutableList<BaseNode> = mutableListOf()
    val connections: MutableList<Connection> = mutableListOf()
    
    var name: String = "Untitled Workflow"
    var description: String = ""
    
    /**
     * Добавить ноду в воркфлоу
     */
    fun addNode(node: BaseNode) {
        nodes.add(node)
    }
    
    /**
     * Удалить ноду из воркфлоу
     */
    fun removeNode(nodeId: String) {
        nodes.removeAll { it.id == nodeId }
        connections.removeAll { it.fromNodeId == nodeId || it.toNodeId == nodeId }
    }
    
    /**
     * Создать соединение между нодами
     */
    fun connectNodes(
        fromNodeId: String,
        fromPort: String,
        toNodeId: String,
        toPort: String
    ) {
        val connection = Connection(
            id = generateConnectionId(),
            fromNodeId = fromNodeId,
            fromPort = fromPort,
            toNodeId = toNodeId,
            toPort = toPort
        )
        connections.add(connection)
    }
    
    /**
     * Удалить соединение
     */
    fun removeConnection(connectionId: String) {
        connections.removeAll { it.id == connectionId }
    }
    
    /**
     * Выполнить весь воркфлоу
     */
    suspend fun execute(): WorkflowResult {
        return withContext(Dispatchers.Default) {
            val results = mutableMapOf<String, NodeResult>()
            val executedNodes = mutableSetOf<String>()
            
            try {
                // Находим входные ноды (без входящих соединений)
                val inputNodes = nodes.filter { node ->
                    connections.none { it.toNodeId == node.id }
                }
                
                if (inputNodes.isEmpty()) {
                    return@withContext WorkflowResult(
                        success = false,
                        error = "Нет входных нод для запуска"
                    )
                }
                
                // Выполняем ноды в порядке зависимостей
                for (node in inputNodes) {
                    executeNodeWithDependencies(node, results, executedNodes)
                }
                
                // Проверяем, все ли ноды выполнены
                val failedNodes = results.filterValues { !it.success }
                
                WorkflowResult(
                    success = failedNodes.isEmpty(),
                    nodeResults = results,
                    failedNodes = failedNodes.keys.toList()
                )
            } catch (e: Exception) {
                WorkflowResult(
                    success = false,
                    error = "Ошибка выполнения: ${e.message}"
                )
            }
        }
    }
    
    private suspend fun executeNodeWithDependencies(
        node: BaseNode,
        results: MutableMap<String, NodeResult>,
        executedNodes: MutableSet<String>
    ) {
        // Если уже выполнена, пропускаем
        if (executedNodes.contains(node.id)) return
        
        // Сначала выполняем зависимости
        val dependencies = connections.filter { it.toNodeId == node.id }
        val inputData = mutableMapOf<String, Any?>()
        
        for (connection in dependencies) {
            val fromNode = nodes.find { it.id == connection.fromNodeId } ?: continue
            
            // Рекурсивно выполняем зависимую ноду
            executeNodeWithDependencies(fromNode, results, executedNodes)
            
            // Получаем результат из предыдущей ноды
            val fromResult = results[connection.fromNodeId]
            if (fromResult?.success == true) {
                inputData[connection.toPort] = fromResult.data[connection.fromPort]
            }
        }
        
        // Добавляем данные из входов ноды
        node.inputs.forEach { input ->
            if (!inputData.containsKey(input.name)) {
                inputData[input.name] = null
            }
        }
        
        // Выполняем ноду
        val result = node.execute(inputData)
        results[node.id] = result
        executedNodes.add(node.id)
    }
    
    /**
     * Экспорт воркфлоу в JSON
     */
    fun exportToJson(): String {
        // Упрощенная сериализация
        val nodesJson = nodes.joinToString(",\n") { node ->
            """{
                "id": "${node.id}",
                "name": "${node.name}",
                "type": "${node.type}",
                "x": ${node.positionX},
                "y": ${node.positionY},
                "apiKey": "${node.apiKey}",
                "apiEndpoint": "${node.apiEndpoint}"
            }"""
        }
        
        val connectionsJson = connections.joinToString(",\n") { conn ->
            """{
                "id": "${conn.id}",
                "from": "${conn.fromNodeId}",
                "to": "${conn.toNodeId}",
                "fromPort": "${conn.fromPort}",
                "toPort": "${conn.toPort}"
            }"""
        }
        
        return """{
            "name": "$name",
            "description": "$description",
            "nodes": [
                $nodesJson
            ],
            "connections": [
                $connectionsJson
            ]
        }"""
    }
    
    /**
     * Очистить воркфлоу
     */
    fun clear() {
        nodes.clear()
        connections.clear()
    }
    
    private fun generateConnectionId(): String {
        return "conn_${System.currentTimeMillis()}_${(Math.random() * 1000).toInt()}"
    }
}

/**
 * Соединение между нодами
 */
data class Connection(
    val id: String,
    val fromNodeId: String,
    val fromPort: String,
    val toNodeId: String,
    val toPort: String
)

/**
 * Результат выполнения воркфлоу
 */
data class WorkflowResult(
    val success: Boolean,
    val nodeResults: Map<String, NodeResult> = emptyMap(),
    val failedNodes: List<String> = emptyList(),
    val error: String? = null
)
