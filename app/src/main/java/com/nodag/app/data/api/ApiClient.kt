package com.nodag.app.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * API Client Builder
 */
object ApiClient {
    
    private const val OPENAI_BASE_URL = "https://api.openai.com/"
    private const val ANTHROPIC_BASE_URL = "https://api.anthropic.com/"
    private const val STABLE_DIFFUSION_BASE_URL = "http://localhost:7860/"
    private const val GOOGLE_AI_BASE_URL = "https://generativelanguage.googleapis.com/"
    
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    
    private val retrofitBuilder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
    
    val openAIService: OpenAIService by lazy {
        retrofitBuilder
            .baseUrl(OPENAI_BASE_URL)
            .build()
            .create(OpenAIService::class.java)
    }
    
    val anthropicService: AnthropicService by lazy {
        retrofitBuilder
            .baseUrl(ANTHROPIC_BASE_URL)
            .build()
            .create(AnthropicService::class.java)
    }
    
    val stableDiffusionService: StableDiffusionService by lazy {
        retrofitBuilder
            .baseUrl(STABLE_DIFFUSION_BASE_URL)
            .build()
            .create(StableDiffusionService::class.java)
    }
    
    val googleAIService: GoogleAIService by lazy {
        retrofitBuilder
            .baseUrl(GOOGLE_AI_BASE_URL)
            .build()
            .create(GoogleAIService::class.java)
    }
}
