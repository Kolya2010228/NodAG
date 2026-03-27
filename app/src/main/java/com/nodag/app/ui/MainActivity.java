package com.nodag.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nodag.app.R;
import com.nodag.app.workflow.WorkflowManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Главный экран - список воркфлоу
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView workflowsRecyclerView;
    private FloatingActionButton fabNewWorkflow;
    private View emptyState;

    private List<WorkflowManager> workflows;
    private WorkflowAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupRecyclerView();
        setupClickListeners();
        loadWorkflows();
    }

    private void initViews() {
        workflowsRecyclerView = findViewById(R.id.workflowsRecyclerView);
        fabNewWorkflow = findViewById(R.id.fabNewWorkflow);
        emptyState = findViewById(R.id.emptyState);
    }

    private void setupRecyclerView() {
        workflows = new ArrayList<>();
        adapter = new WorkflowAdapter(workflows, this::openWorkflow);
        workflowsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        workflowsRecyclerView.setAdapter(adapter);
    }

    private void setupClickListeners() {
        fabNewWorkflow.setOnClickListener(v -> createNewWorkflow());
    }

    private void loadWorkflows() {
        // Загружаем сохраненные воркфлоу (пока пусто)
        updateEmptyState();
    }

    private void createNewWorkflow() {
        Intent intent = new Intent(this, WorkflowEditorActivity.class);
        startActivity(intent);
    }

    private void openWorkflow(WorkflowManager workflow) {
        Intent intent = new Intent(this, WorkflowEditorActivity.class);
        // TODO: Передать ID воркфлоу
        startActivity(intent);
    }

    private void updateEmptyState() {
        emptyState.setVisibility(workflows.isEmpty() ? View.VISIBLE : View.GONE);
        workflowsRecyclerView.setVisibility(workflows.isEmpty() ? View.GONE : View.VISIBLE);
    }
}
