package com.nodag.app.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.nodag.app.data.model.Workflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val Context.workflowDataStore: DataStore<Preferences> by preferencesDataStore(name = "workflows")

/**
 * Repository для управления воркфлоу
 */
class WorkflowRepository(private val context: Context) {
    
    private val json = Json { ignoreUnknownKeys = true }
    
    val workflowsFlow: Flow<List<Workflow>> = context.workflowDataStore.data.map { preferences ->
        val workflowsJson = preferences[stringPreferencesKey("workflows")] ?: "[]"
        try {
            json.decodeFromString(workflowsJson)
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    suspend fun saveWorkflow(workflow: Workflow) {
        context.workflowDataStore.edit { preferences ->
            val workflows = workflowsFlow.value.toMutableList()
            val existingIndex = workflows.indexOfFirst { it.id == workflow.id }
            if (existingIndex >= 0) {
                workflows[existingIndex] = workflow.copy(updatedAt = System.currentTimeMillis())
            } else {
                workflows.add(workflow)
            }
            preferences[stringPreferencesKey("workflows")] = json.encodeToString(workflows)
        }
    }
    
    suspend fun deleteWorkflow(workflowId: String) {
        context.workflowDataStore.edit { preferences ->
            val workflows = workflowsFlow.value.filter { it.id != workflowId }
            preferences[stringPreferencesKey("workflows")] = json.encodeToString(workflows)
        }
    }
    
    suspend fun getWorkflowById(workflowId: String): Workflow? {
        return workflowsFlow.value.find { it.id == workflowId }
    }
    
    suspend fun updateLastOpened(workflowId: String) {
        val workflow = getWorkflowById(workflowId) ?: return
        saveWorkflow(
            workflow.copy(
                lastOpenedAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
        )
    }
}
