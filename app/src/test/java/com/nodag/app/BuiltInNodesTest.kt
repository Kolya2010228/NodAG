package com.nodag.app.nodes

import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

/**
 * Тесты для базовых нод
 */
class BuiltInNodesTest {

    @Test
    fun inputPromptNode_executeWithText_returnsSuccess() = runTest {
        val node = InputPromptNode("test_id", "Test Prompt")
        node.defaultPrompt = "Hello, AI!"

        val result = node.execute(emptyMap())

        assertTrue(result.success)
        assertEquals("Hello, AI!", result.data["Текст"])
    }

    @Test
    fun inputPromptNode_executeWithEmptyPrompt_returnsFailure() = runTest {
        val node = InputPromptNode("test_id", "Test Prompt")
        node.defaultPrompt = ""

        val result = node.execute(emptyMap())

        assertFalse(result.success)
        assertNotNull(result.error)
    }

    @Test
    fun inputPromptNode_validate_returnsTrue() {
        val node = InputPromptNode("test_id", "Test Prompt")

        assertTrue(node.validate())
    }

    @Test
    fun inputPromptNode_clone_createsCopy() {
        val original = InputPromptNode("test_id", "Test Prompt")
        original.defaultPrompt = "Test prompt"
        original.positionX = 100f
        original.positionY = 200f

        val clone = original.clone() as InputPromptNode

        assertEquals(original.id, clone.id)
        assertEquals(original.name, clone.name)
        assertEquals(original.defaultPrompt, clone.defaultPrompt)
        assertEquals(original.positionX, clone.positionX, 0.01f)
        assertEquals(original.positionY, clone.positionY, 0.01f)
    }

    @Test
    fun outputNode_executeWithText_returnsSuccess() = runTest {
        val node = OutputNode("test_id", "Output")

        val result = node.execute(mapOf("Текст" to "Test output"))

        assertTrue(result.success)
        assertEquals("Test output", node.outputText)
    }

    @Test
    fun customNode_validate_withoutEndpoint_returnsFalse() {
        val node = CustomNode("test_id", "Custom Node")
        node.apiEndpoint = ""

        assertFalse(node.validate())
    }

    @Test
    fun customNode_validate_withEndpoint_returnsTrue() {
        val node = CustomNode("test_id", "Custom Node")
        node.apiEndpoint = "https://api.example.com"

        assertTrue(node.validate())
    }
}
