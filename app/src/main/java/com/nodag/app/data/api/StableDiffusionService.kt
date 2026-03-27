package com.nodag.app.data.api

import retrofit2.http.*

/**
 * Stable Diffusion API Service
 */
interface StableDiffusionService {
    
    @POST("sdapi/v1/txt2img")
    suspend fun textToImage(
        @Body request: SDRequest
    ): SDResponse
    
    @POST("sdapi/v1/img2img")
    suspend fun imageToImage(
        @Body request: SDRequest
    ): SDResponse
}

data class SDRequest(
    val prompt: String,
    val negative_prompt: String = "",
    val steps: Int = 20,
    val width: Int = 512,
    val height: Int = 512,
    val cfg_scale: Double = 7.0,
    val seed: Int = -1
)

data class SDResponse(
    val images: List<String>,
    val parameters: SDParameters,
    val info: String
)

data class SDParameters(
    val prompt: String,
    val steps: Int,
    val sampler: String,
    val cfg_scale: Double
)
