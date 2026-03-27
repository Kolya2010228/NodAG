package com.nodag.app.data.model

import kotlinx.serialization.Serializable

/**
 * Соединение между двумя портами нод
 */
@Serializable
data class NodeConnection(
    val id: String = java.util.UUID.randomUUID().toString(),
    val fromNodeId: String,
    val fromPortId: String,
    val toNodeId: String,
    val toPortId: String
)
