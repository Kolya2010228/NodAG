package com.nodag.app.data.api

import retrofit2.http.*

/**
 * Google AI (Gemini) API Service
 */
interface GoogleAIService {
    
    @POST("v1beta/models/{model}:generateContent")
    suspend fun generateContent(
        @Path("model") model: String = "gemini-pro",
        @Query("key") apiKey: String,
        @Body request: GoogleAIRequest
    ): GoogleAIResponse
}

data class GoogleAIRequest(
    val contents: List<GoogleAIContent>,
    val generationConfig: GenerationConfig? = null
)

data class GoogleAIContent(
    val parts: List<GoogleAIPart>
)

data class GoogleAIPart(
    val text: String
)

data class GenerationConfig(
    val temperature: Double? = null,
    val maxOutputTokens: Int? = null,
    val topP: Double? = null,
    val topK: Int? = null
)

data class GoogleAIResponse(
    val candidates: List<Candidate>,
    val usageMetadata: UsageMetadata?
)

data class Candidate(
    val content: GoogleAIContent,
    val finishReason: String?,
    val index: Int
)

data class UsageMetadata(
    val promptTokenCount: Int,
    val candidatesTokenCount: Int,
    val totalTokenCount: Int
)
