package com.nodag.app.data.api

import retrofit2.http.*

/**
 * Anthropic Claude API Service
 */
interface AnthropicService {
    
    @POST("v1/messages")
    @Headers("Content-Type: application/json")
    suspend fun createMessage(
        @Header("x-api-key") apiKey: String,
        @Header("anthropic-version") version: String = "2023-06-01",
        @Body request: AnthropicRequest
    ): AnthropicResponse
}

data class AnthropicRequest(
    val model: String = "claude-3-opus-20240229",
    val max_tokens: Int = 1024,
    val messages: List<AnthropicMessage>,
    val system: String? = null
)

data class AnthropicMessage(
    val role: String,
    val content: String
)

data class AnthropicResponse(
    val id: String,
    val type: String,
    val role: String,
    val content: List<AnthropicContent>,
    val stop_reason: String?,
    val usage: AnthropicUsage
)

data class AnthropicContent(
    val type: String,
    val text: String
)

data class AnthropicUsage(
    val input_tokens: Int,
    val output_tokens: Int
)
