package com.nodag.app.data.model

import kotlinx.serialization.Serializable

/**
 * API ключ для сервиса
 */
@Serializable
data class ApiKey(
    val id: String = java.util.UUID.randomUUID().toString(),
    val serviceType: ServiceType,
    val key: String,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Настройки приложения
 */
@Serializable
data class AppSettings(
    val theme: Theme = Theme.DARK,
    val language: Language = Language.RUSSIAN,
    val exportFormat: ExportFormat = ExportFormat.JSON,
    val notificationsEnabled: Boolean = true
)

@Serializable
enum class Theme {
    DARK,
    LIGHT,
    SYSTEM
}

@Serializable
enum class Language {
    RUSSIAN,
    ENGLISH
}

@Serializable
enum class ExportFormat {
    JSON,
    PNG,
    BOTH
}
