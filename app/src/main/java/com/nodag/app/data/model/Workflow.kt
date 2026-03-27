package com.nodag.app.data.model

import kotlinx.serialization.Serializable

/**
 * Воркфлоу - коллекция нод и соединений
 */
@Serializable
data class Workflow(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val description: String? = null,
    val nodes: List<Node> = emptyList(),
    val connections: List<NodeConnection> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val lastOpenedAt: Long? = null
) {
    fun getNodeCount(): Int = nodes.size
    
    fun getFormattedLastOpened(): String {
        val lastOpened = lastOpenedAt ?: createdAt
        val now = System.currentTimeMillis()
        val diff = now - lastOpened
        
        return when {
            diff < 60 * 60 * 1000 -> "${diff / (60 * 1000)} мин назад"
            diff < 24 * 60 * 60 * 1000 -> "${diff / (60 * 60 * 1000)} ч назад"
            diff < 7 * 24 * 60 * 60 * 1000 -> "${diff / (24 * 60 * 60 * 1000)} дн назад"
            else -> {
                val date = java.util.Date(lastOpened)
                java.text.SimpleDateFormat("dd MMM", java.util.Locale.getDefault()).format(date)
            }
        }
    }
}
