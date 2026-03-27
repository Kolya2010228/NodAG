package com.nodag.app.domain

import com.nodag.app.data.api.*
import com.nodag.app.data.model.*

/**
 * Исполнитель воркфлоу - запускает ноды последовательно
 */
class WorkflowExecutor(
    private val openAIService: OpenAIService,
    private val anthropicService: AnthropicService,
    private val stableDiffusionService: StableDiffusionService,
    private val googleAIService: GoogleAIService
) {
    
    /**
     * Выполнить ноду и вернуть результат
     */
    suspend fun executeNode(
        node: Node,
        inputs: Map<String, String>,
        apiKey: String?
    ): NodeExecutionResult {
        return when (node.serviceType) {
            ServiceType.OPENAI -> executeOpenAI(node, inputs, apiKey)
            ServiceType.ANTHROPIC -> executeAnthropic(node, inputs, apiKey)
            ServiceType.STABLE_DIFFUSION -> executeStableDiffusion(node, inputs, apiKey)
            ServiceType.GOOGLE_AI -> executeGoogleAI(node, inputs, apiKey)
            ServiceType.CUSTOM -> executeCustom(node, inputs, apiKey)
            null -> NodeExecutionResult.Success(inputs)
        }
    }
    
    private suspend fun executeOpenAI(
        node: Node,
        inputs: Map<String, String>,
        apiKey: String?
    ): NodeExecutionResult {
        try {
            val prompt = inputs["prompt"] ?: ""
            val request = OpenAIRequest(
                model = node.config.parameters["model"] as? String ?: "gpt-4",
                messages = listOf(Message("user", prompt)),
                temperature = (node.config.parameters["temperature"] as? Number)?.toDouble() ?: 0.7,
                max_tokens = node.config.parameters["max_tokens"] as? Int
            )
            
            val response = openAIService.createChatCompletion(
                apiKey = "Bearer ${apiKey.orEmpty()}",
                request = request
            )
            
            val result = response.choices.firstOrNull()?.message?.content.orEmpty()
            return NodeExecutionResult.Success(
                mapOf(
                    "response" to result,
                    "usage" to "${response.usage.total_tokens} tokens"
                )
            )
        } catch (e: Exception) {
            return NodeExecutionResult.Error(e.message ?: "OpenAI API error")
        }
    }
    
    private suspend fun executeAnthropic(
        node: Node,
        inputs: Map<String, String>,
        apiKey: String?
    ): NodeExecutionResult {
        try {
            val prompt = inputs["prompt"] ?: ""
            val request = AnthropicRequest(
                model = node.config.parameters["model"] as? String ?: "claude-3-opus-20240229",
                max_tokens = node.config.parameters["max_tokens"] as? Int ?: 1024,
                messages = listOf(AnthropicMessage("user", prompt))
            )
            
            val response = anthropicService.createMessage(
                apiKey = apiKey.orEmpty(),
                request = request
            )
            
            val result = response.content.firstOrNull()?.text.orEmpty()
            return NodeExecutionResult.Success(
                mapOf(
                    "response" to result,
                    "usage" to "${response.usage.input_tokens + response.usage.output_tokens} tokens"
                )
            )
        } catch (e: Exception) {
            return NodeExecutionResult.Error(e.message ?: "Anthropic API error")
        }
    }
    
    private suspend fun executeStableDiffusion(
        node: Node,
        inputs: Map<String, String>,
        apiKey: String?
    ): NodeExecutionResult {
        try {
            val prompt = inputs["prompt"] ?: ""
            val request = SDRequest(
                prompt = prompt,
                steps = node.config.parameters["steps"] as? Int ?: 20,
                width = node.config.parameters["width"] as? Int ?: 512,
                height = node.config.parameters["height"] as? Int ?: 512
            )
            
            val response = stableDiffusionService.textToImage(request)
            
            val imageUrl = response.images.firstOrNull().orEmpty()
            return NodeExecutionResult.Success(
                mapOf("image" to imageUrl)
            )
        } catch (e: Exception) {
            return NodeExecutionResult.Error(e.message ?: "Stable Diffusion API error")
        }
    }
    
    private suspend fun executeGoogleAI(
        node: Node,
        inputs: Map<String, String>,
        apiKey: String?
    ): NodeExecutionResult {
        try {
            val prompt = inputs["prompt"] ?: ""
            val request = GoogleAIRequest(
                contents = listOf(
                    GoogleAIContent(
                        parts = listOf(GoogleAIPart(prompt))
                    )
                )
            )
            
            val response = googleAIService.generateContent(
                apiKey = apiKey.orEmpty(),
                request = request
            )
            
            val result = response.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text.orEmpty()
            return NodeExecutionResult.Success(
                mapOf("response" to result)
            )
        } catch (e: Exception) {
            return NodeExecutionResult.Error(e.message ?: "Google AI API error")
        }
    }
    
    private suspend fun executeCustom(
        node: Node,
        inputs: Map<String, String>,
        apiKey: String?
    ): NodeExecutionResult {
        // TODO: Реализовать вызов кастомного API
        return NodeExecutionResult.Success(inputs)
    }
    
    /**
     * Выполнить весь воркфлоу
     */
    suspend fun executeWorkflow(
        workflow: Workflow,
        initialInputs: Map<String, String>,
        apiKeys: Map<ServiceType, String>
    ): WorkflowExecutionResult {
        val nodeResults = mutableMapOf<String, NodeExecutionResult>()
        val executedNodes = mutableSetOf<String>()
        
        // Топологическая сортировка и выполнение
        fun executeNodeWithDependencies(nodeId: String): NodeExecutionResult? {
            if (executedNodes.contains(nodeId)) {
                return nodeResults[nodeId]
            }
            
            val node = workflow.nodes.find { it.id == nodeId } ?: return null
            
            // Собрать входы из соединений
            val inputs = mutableMapOf<String, String>()
            workflow.connections
                .filter { it.toNodeId == nodeId }
                .forEach { connection ->
                    val fromResult = executeNodeWithDependencies(connection.fromNodeId)
                    fromResult?.let { result ->
                        if (result is NodeExecutionResult.Success) {
                            inputs.putAll(result.outputs)
                        }
                    }
                }
            
            // Добавить начальные входы
            inputs.putAll(initialInputs)
            
            // Выполнить ноду
            val apiKey = node.serviceType?.let { apiKeys[it] }
            val result = executeNode(node, inputs, apiKey)
            
            nodeResults[nodeId] = result
            executedNodes.add(nodeId)
            
            return result
        }
        
        // Выполнить все ноды
        workflow.nodes.forEach { node ->
            executeNodeWithDependencies(node.id)
        }
        
        // Собрать выходы
        val outputs = workflow.nodes
            .filter { it.type == NodeType.OUTPUT }
            .flatMap { node ->
                nodeResults[node.id]?.let { result ->
                    if (result is NodeExecutionResult.Success) {
                        result.outputs.entries.map { "${node.name}.${it.key}" to it.value }
                    } else {
                        emptyList()
                    }
                } ?: emptyList()
            }
            .toMap()
        
        return WorkflowExecutionResult(
            nodeResults = nodeResults,
            outputs = outputs
        )
    }
}

sealed class NodeExecutionResult {
    data class Success(val outputs: Map<String, String>) : NodeExecutionResult()
    data class Error(val message: String) : NodeExecutionResult()
}

data class WorkflowExecutionResult(
    val nodeResults: Map<String, NodeExecutionResult>,
    val outputs: Map<String, String>
)
