package com.nodag.app.data.api

import retrofit2.http.*

/**
 * OpenAI API Service
 */
interface OpenAIService {
    
    @POST("v1/chat/completions")
    @Headers("Content-Type: application/json")
    suspend fun createChatCompletion(
        @Header("Authorization") apiKey: String,
        @Body request: OpenAIRequest
    ): OpenAIResponse
    
    @POST("v1/images/generations")
    @Headers("Content-Type: application/json")
    suspend fun createImage(
        @Header("Authorization") apiKey: String,
        @Body request: OpenAIImageRequest
    ): OpenAIImageResponse
}

data class OpenAIRequest(
    val model: String = "gpt-4",
    val messages: List<Message>,
    val temperature: Double = 0.7,
    val max_tokens: Int? = null
)

data class Message(
    val role: String,
    val content: String
)

data class OpenAIResponse(
    val id: String,
    val choices: List<Choice>,
    val usage: Usage
)

data class Choice(
    val message: Message,
    val finish_reason: String
)

data class Usage(
    val prompt_tokens: Int,
    val completion_tokens: Int,
    val total_tokens: Int
)

data class OpenAIImageRequest(
    val prompt: String,
    val n: Int = 1,
    val size: String = "1024x1024"
)

data class OpenAIImageResponse(
    val data: List<ImageData>
)

data class ImageData(
    val url: String
)
