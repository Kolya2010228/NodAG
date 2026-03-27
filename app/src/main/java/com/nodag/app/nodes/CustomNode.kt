package com.nodag.app.nodes

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

/**
 * Кастомная нода, создаваемая пользователем
 * Позволяет подключаться к любому ИИ API
 */
class CustomNode(
    id: String,
    name: String,
    val customType: String = "API_CALL"
) : BaseNode(id, name, NodeType.CUSTOM) {

    var httpMethod: String = "POST"
    var requestBodyTemplate: String = ""
    var responseMapping: Map<String, String> = emptyMap()
    var headers: Map<String, String> = emptyMap()
    
    init {
        // По умолчанию добавляем стандартные порты
        addInput("Промпт", PortType.TEXT)
        addInput("API Key", PortType.TEXT)
        addOutput("Ответ", PortType.TEXT)
    }
    
    override suspend fun execute(inputData: Map<String, Any?>): NodeResult {
        return withContext(Dispatchers.IO) {
            try {
                val prompt = inputData["Промпт"] as? String ?: ""
                val apiKey = inputData["API Key"] as? String ?: this@CustomNode.apiKey
                
                if (apiEndpoint.isBlank()) {
                    return@withContext NodeResult.failure("API Endpoint не указан")
                }
                
                if (apiKey.isBlank()) {
                    return@withContext NodeResult.failure("API Key не указан")
                }
                
                // Формируем тело запроса
                val requestBody = if (requestBodyTemplate.isNotBlank()) {
                    requestBodyTemplate.replace("{prompt}", prompt)
                } else {
                    """{"prompt": "$prompt"}"""
                }
                
                // Отправляем запрос
                val response = sendHttpRequest(apiEndpoint, apiKey, requestBody)
                
                // Парсим ответ согласно маппингу
                val resultData = parseResponse(response)
                
                NodeResult.success(resultData)
            } catch (e: Exception) {
                NodeResult.failure("Ошибка выполнения: ${e.message}")
            }
        }
    }
    
    private fun sendHttpRequest(urlString: String, apiKey: String, body: String): String {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        
        try {
            connection.requestMethod = httpMethod
            connection.doOutput = true
            connection.connectTimeout = 30000
            connection.readTimeout = 30000
            
            // Заголовки
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Authorization", "Bearer $apiKey")
            
            headers.forEach { (key, value) ->
                connection.setRequestProperty(key, value)
            }
            
            // Тело запроса
            connection.outputStream.use { os ->
                os.write(body.toByteArray())
            }
            
            // Читаем ответ
            val responseCode = connection.responseCode
            val inputStream = if (responseCode >= 400) {
                connection.errorStream
            } else {
                connection.inputStream
            }
            
            return inputStream?.bufferedReader()?.use { it.readText() } ?: ""
        } finally {
            connection.disconnect()
        }
    }
    
    private fun parseResponse(response: String): Map<String, Any?> {
        // Простой парсинг JSON ответа
        return try {
            val result = mutableMapOf<String, Any?>()
            
            if (responseMapping.isEmpty()) {
                // Если маппинг не настроен, возвращаем весь ответ
                result["Ответ"] = response
            } else {
                // Парсим согласно маппингу (упрощенно)
                responseMapping.forEach { (outputPort, jsonPath) ->
                    val value = extractJsonValue(response, jsonPath)
                    result[outputPort] = value
                }
            }
            
            result
        } catch (e: Exception) {
            mapOf("Ответ" to response)
        }
    }
    
    private fun extractJsonValue(json: String, path: String): String {
        // Упрощенное извлечение значения по пути (например "choices[0].message.content")
        // В реальной реализации нужно использовать полноценный JSON парсер
        return json
    }
    
    override fun validate(): Boolean {
        return super.validate() && apiEndpoint.isNotBlank()
    }
    
    override fun clone(): BaseNode {
        val clone = CustomNode(id, name, customType)
        clone.inputs.addAll(inputs)
        clone.outputs.addAll(outputs)
        clone.apiKey = apiKey
        clone.apiEndpoint = apiEndpoint
        clone.httpMethod = httpMethod
        clone.requestBodyTemplate = requestBodyTemplate
        clone.responseMapping = responseMapping
        clone.headers = headers
        clone.positionX = positionX
        clone.positionY = positionY
        return clone
    }
    
    /**
     * Конфигурация кастомной ноды
     */
    data class Config(
        val name: String,
        val apiEndpoint: String,
        val apiKey: String,
        val httpMethod: String = "POST",
        val requestBodyTemplate: String = "",
        val inputs: List<PortConfig> = emptyList(),
        val outputs: List<PortConfig> = emptyList(),
        val responseMapping: Map<String, String> = emptyMap()
    )
    
    data class PortConfig(
        val name: String,
        val type: PortType,
        val direction: PortDirection
    )
}
