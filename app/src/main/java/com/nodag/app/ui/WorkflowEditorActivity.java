package com.nodag.app.ui;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.nodag.app.R;
import com.nodag.app.nodes.*;
import com.nodag.app.workflow.WorkflowManager;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Экран редактора воркфлоу
 */
public class WorkflowEditorActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView builtInNodesRecyclerView;
    private RecyclerView customNodesRecyclerView;
    private View workflowCanvas;
    private TextView statusText;
    private MaterialButton btnRun;

    private WorkflowManager workflowManager;
    private List<NodeTemplate> builtInNodes;
    private List<NodeTemplate> customNodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workflow_editor);

        workflowManager = new WorkflowManager();
        builtInNodes = new ArrayList<>();
        customNodes = new ArrayList<>();

        initViews();
        setupToolbar();
        setupNodeLists();
        setupClickListeners();
        loadBuiltInNodes();
        loadCustomNodes();
        updateStatus();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        builtInNodesRecyclerView = findViewById(R.id.builtInNodesRecyclerView);
        customNodesRecyclerView = findViewById(R.id.customNodesRecyclerView);
        workflowCanvas = findViewById(R.id.workflowCanvas);
        statusText = findViewById(R.id.statusText);
        btnRun = findViewById(R.id.btnRun);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Редактор воркфлоу");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupNodeLists() {
        builtInNodesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        builtInNodesRecyclerView.setAdapter(new NodeTemplateAdapter(builtInNodes, this::addNodeToWorkflow));

        customNodesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        customNodesRecyclerView.setAdapter(new NodeTemplateAdapter(customNodes, this::addNodeToWorkflow));
    }

    private void setupClickListeners() {
        btnRun.setOnClickListener(v -> runWorkflow());
    }

    private void loadBuiltInNodes() {
        builtInNodes.add(new NodeTemplate("Input Prompt", NodeType.INPUT, "📝"));
        builtInNodes.add(new NodeTemplate("Input File", NodeType.INPUT, "📥"));
        builtInNodes.add(new NodeTemplate("Output", NodeType.OUTPUT, "📤"));
        builtInNodes.add(new NodeTemplate("GPT-4", NodeType.PROCESSING, "🤖"));
        builtInNodes.add(new NodeTemplate("Claude", NodeType.PROCESSING, "🤖"));
        builtInNodes.add(new NodeTemplate("Gemini", NodeType.PROCESSING, "🤖"));
        builtInNodesRecyclerView.getAdapter().notifyDataSetChanged();
    }

    private void loadCustomNodes() {
        // Загружаем сохраненные кастомные ноды
        // Пока пусто
    }

    private void addNodeToWorkflow(NodeTemplate template) {
        String nodeId = UUID.randomUUID().toString();
        BaseNode node;

        switch (template.getName()) {
            case "Input Prompt":
                node = new InputPromptNode(nodeId);
                break;
            case "Input File":
                node = new InputFileNode(nodeId);
                break;
            case "Output":
                node = new OutputNode(nodeId);
                break;
            case "GPT-4":
                node = createGPTNode(nodeId);
                break;
            case "Claude":
                node = createClaudeNode(nodeId);
                break;
            case "Gemini":
                node = createGeminiNode(nodeId);
                break;
            default:
                node = new CustomNode(nodeId, template.getName());
                break;
        }

        node.setPositionX(100f);
        node.setPositionY(100f);

        workflowManager.addNode(node);
        updateStatus();
    }

    private CustomNode createGPTNode(String nodeId) {
        CustomNode node = new CustomNode(nodeId, "GPT-4");
        node.setApiEndpoint("https://api.openai.com/v1/chat/completions");
        return node;
    }

    private CustomNode createClaudeNode(String nodeId) {
        CustomNode node = new CustomNode(nodeId, "Claude");
        node.setApiEndpoint("https://api.anthropic.com/v1/messages");
        return node;
    }

    private CustomNode createGeminiNode(String nodeId) {
        CustomNode node = new CustomNode(nodeId, "Gemini");
        node.setApiEndpoint("https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent");
        return node;
    }

    private void runWorkflow() {
        btnRun.setEnabled(false);
        statusText.setText("Выполнение...");

        CompletableFuture<WorkflowResult> future = workflowManager.execute();
        future.whenComplete((result, exception) -> {
            runOnUiThread(() -> {
                if (exception != null) {
                    statusText.setText("Ошибка: " + exception.getMessage());
                } else {
                    statusText.setText("Готово | Нод: " + workflowManager.getNodes().size());
                }
                btnRun.setEnabled(true);
            });
        });
    }

    private void updateStatus() {
        statusText.setText("Готово | Нод: " + workflowManager.getNodes().size());
    }
}
