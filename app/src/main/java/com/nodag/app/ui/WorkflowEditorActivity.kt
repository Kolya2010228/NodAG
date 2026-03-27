package com.nodag.app.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.nodag.app.R
import com.nodag.app.nodes.*
import com.nodag.app.workflow.WorkflowManager
import java.util.UUID

/**
 * Экран редактора воркфлоу
 */
class WorkflowEditorActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var builtInNodesRecyclerView: RecyclerView
    private lateinit var customNodesRecyclerView: RecyclerView
    private lateinit var workflowCanvas: View
    private lateinit var statusText: TextView
    private lateinit var btnRun: MaterialButton

    private val workflowManager = WorkflowManager()
    
    private val builtInNodes = listOf(
        NodeTemplate("Input Prompt", NodeType.INPUT, "📝"),
        NodeTemplate("Input File", NodeType.INPUT, "📥"),
        NodeTemplate("Output", NodeType.OUTPUT, "📤"),
        NodeTemplate("GPT-4", NodeType.PROCESSING, "🤖"),
        NodeTemplate("Claude", NodeType.PROCESSING, "🤖"),
        NodeTemplate("Gemini", NodeType.PROCESSING, "🤖")
    )
    
    private val customNodes = mutableListOf<NodeTemplate>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workflow_editor)

        initViews()
        setupToolbar()
        setupNodeLists()
        setupClickListeners()
        loadCustomNodes()
        updateStatus()
    }

    private fun initViews() {
        toolbar = findViewById(R.id.toolbar)
        builtInNodesRecyclerView = findViewById(R.id.builtInNodesRecyclerView)
        customNodesRecyclerView = findViewById(R.id.customNodesRecyclerView)
        workflowCanvas = findViewById(R.id.workflowCanvas)
        statusText = findViewById(R.id.statusText)
        btnRun = findViewById(R.id.btnRun)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupNodeLists() {
        // Встроенные ноды
        builtInNodesRecyclerView.layoutManager = LinearLayoutManager(this)
        builtInNodesRecyclerView.adapter = NodeTemplateAdapter(builtInNodes) { template ->
            addNodeToWorkflow(template)
        }

        // Кастомные ноды
        customNodesRecyclerView.layoutManager = LinearLayoutManager(this)
        customNodesRecyclerView.adapter = NodeTemplateAdapter(customNodes) { template ->
            addNodeToWorkflow(template)
        }
    }

    private fun setupClickListeners() {
        btnRun.setOnClickListener {
            runWorkflow()
        }
    }

    private fun loadCustomNodes() {
        // Загружаем сохраненные кастомные ноды
        // Пока пусто
    }

    private fun addNodeToWorkflow(template: NodeTemplate) {
        val nodeId = UUID.randomUUID().toString()
        
        val node = when (template.name) {
            "Input Prompt" -> InputPromptNode(nodeId)
            "Input File" -> InputFileNode(nodeId)
            "Output" -> OutputNode(nodeId)
            "GPT-4" -> createGPTNode(nodeId)
            "Claude" -> createClaudeNode(nodeId)
            "Gemini" -> createGeminiNode(nodeId)
            else -> CustomNode(nodeId, template.name)
        }
        
        // Устанавливаем позицию в центре канваса
        node.positionX = 100f
        node.positionY = 100f
        
        workflowManager.addNode(node)
        updateStatus()
    }

    private fun createGPTNode(nodeId: String): CustomNode {
        val node = CustomNode(nodeId, "GPT-4")
        node.apiEndpoint = "https://api.openai.com/v1/chat/completions"
        return node
    }

    private fun createClaudeNode(nodeId: String): CustomNode {
        val node = CustomNode(nodeId, "Claude")
        node.apiEndpoint = "https://api.anthropic.com/v1/messages"
        return node
    }

    private fun createGeminiNode(nodeId: String): CustomNode {
        val node = CustomNode(nodeId, "Gemini")
        node.apiEndpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent"
        return node
    }

    private fun runWorkflow() {
        // Запуск воркфлоу
        btnRun.isEnabled = false
        statusText.text = "Выполнение..."
        
        // В реальном приложении здесь будет запуск корутины
        workflowManager.execute()
            .invokeOnCompletion { exception ->
                runOnUiThread {
                    if (exception != null) {
                        statusText.text = "Ошибка: ${exception.message}"
                    } else {
                        statusText.text = "Готово | Нод: ${workflowManager.nodes.size}"
                    }
                    btnRun.isEnabled = true
                }
            }
    }

    private fun updateStatus() {
        statusText.text = "Готово | Нод: ${workflowManager.nodes.size}"
    }
}

/**
 * Шаблон ноды для отображения в списке
 */
data class NodeTemplate(
    val name: String,
    val type: NodeType,
    val icon: String
)
