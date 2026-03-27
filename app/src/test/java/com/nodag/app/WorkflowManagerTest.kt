package com.nodag.app.workflow

import com.nodag.app.nodes.InputPromptNode
import com.nodag.app.nodes.OutputNode
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

/**
 * Тесты для WorkflowManager
 */
class WorkflowManagerTest {

    @Test
    fun addNode_increasesNodesCount() {
        val workflow = WorkflowManager()
        val node = InputPromptNode("node1")

        workflow.addNode(node)

        assertEquals(1, workflow.nodes.size)
    }

    @Test
    fun removeNode_decreasesNodesCount() {
        val workflow = WorkflowManager()
        val node = InputPromptNode("node1")
        workflow.addNode(node)

        workflow.removeNode("node1")

        assertEquals(0, workflow.nodes.size)
    }

    @Test
    fun connectNodes_createsConnection() {
        val workflow = WorkflowManager()
        val node1 = InputPromptNode("node1")
        val node2 = OutputNode("node2")
        workflow.addNode(node1)
        workflow.addNode(node2)

        workflow.connectNodes("node1", "Текст", "node2", "Текст")

        assertEquals(1, workflow.connections.size)
        assertEquals("node1", workflow.connections[0].fromNodeId)
        assertEquals("node2", workflow.connections[0].toNodeId)
    }

    @Test
    fun execute_emptyWorkflow_returnsFailure() = runTest {
        val workflow = WorkflowManager()

        val result = workflow.execute()

        assertFalse(result.success)
        assertNotNull(result.error)
    }

    @Test
    fun execute_singleNodeWorkflow_returnsSuccess() = runTest {
        val workflow = WorkflowManager()
        val node = InputPromptNode("node1")
        node.defaultPrompt = "Test"
        workflow.addNode(node)

        val result = workflow.execute()

        assertTrue(result.success)
        assertTrue(result.nodeResults.containsKey("node1"))
    }

    @Test
    fun clear_removesAllNodesAndConnections() {
        val workflow = WorkflowManager()
        workflow.addNode(InputPromptNode("node1"))
        workflow.addNode(OutputNode("node2"))
        workflow.connectNodes("node1", "Текст", "node2", "Текст")

        workflow.clear()

        assertEquals(0, workflow.nodes.size)
        assertEquals(0, workflow.connections.size)
    }

    @Test
    fun exportToJson_returnsValidJson() {
        val workflow = WorkflowManager()
        workflow.name = "Test Workflow"
        workflow.addNode(InputPromptNode("node1", "Input"))

        val json = workflow.exportToJson()

        assertTrue(json.contains("Test Workflow"))
        assertTrue(json.contains("node1"))
        assertTrue(json.contains("nodes"))
    }
}
