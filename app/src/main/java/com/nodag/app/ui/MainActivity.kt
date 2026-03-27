package com.nodag.app.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.nodag.app.R
import com.nodag.app.workflow.WorkflowManager

/**
 * Главный экран - список воркфлоу
 */
class MainActivity : AppCompatActivity() {

    private lateinit var workflowsRecyclerView: RecyclerView
    private lateinit var fabNewWorkflow: FloatingActionButton
    private lateinit var emptyState: View
    
    private val workflows = mutableListOf<WorkflowManager>()
    private lateinit var adapter: WorkflowAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupRecyclerView()
        setupClickListeners()
        loadWorkflows()
    }

    private fun initViews() {
        workflowsRecyclerView = findViewById(R.id.workflowsRecyclerView)
        fabNewWorkflow = findViewById(R.id.fabNewWorkflow)
        emptyState = findViewById(R.id.emptyState)
    }

    private fun setupRecyclerView() {
        adapter = WorkflowAdapter(workflows) { workflow ->
            openWorkflow(workflow)
        }
        workflowsRecyclerView.layoutManager = LinearLayoutManager(this)
        workflowsRecyclerView.adapter = adapter
    }

    private fun setupClickListeners() {
        fabNewWorkflow.setOnClickListener {
            createNewWorkflow()
        }
    }

    private fun loadWorkflows() {
        // Загружаем сохраненные воркфлоу (пока пусто)
        updateEmptyState()
    }

    private fun createNewWorkflow() {
        val intent = Intent(this, WorkflowEditorActivity::class.java)
        startActivity(intent)
    }

    private fun openWorkflow(workflow: WorkflowManager) {
        val intent = Intent(this, WorkflowEditorActivity::class.java)
        // TODO: Передать ID воркфлоу
        startActivity(intent)
    }

    private fun updateEmptyState() {
        emptyState.visibility = if (workflows.isEmpty()) View.VISIBLE else View.GONE
        workflowsRecyclerView.visibility = if (workflows.isEmpty()) View.GONE else View.VISIBLE
    }
}
