package com.nodag.app.ui;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.nodag.app.R;
import com.nodag.app.nodes.CustomNode;
import com.nodag.app.nodes.PortDirection;
import com.nodag.app.nodes.PortType;
import java.util.ArrayList;
import java.util.List;

/**
 * Экран создания кастомной ноды
 */
public class CreateNodeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextInputEditText nodeNameInput;
    private TextInputEditText apiEndpointInput;
    private TextInputEditText apiKeyInput;
    private RecyclerView inputsRecyclerView;
    private RecyclerView outputsRecyclerView;
    private MaterialButton btnAddInput;
    private MaterialButton btnAddOutput;
    private MaterialButton btnCancel;
    private MaterialButton btnSave;

    private List<PortItem> portInputs;
    private List<PortItem> portOutputs;
    private PortAdapter inputsAdapter;
    private PortAdapter outputsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_node);

        portInputs = new ArrayList<>();
        portOutputs = new ArrayList<>();

        initViews();
        setupToolbar();
        setupRecyclerViews();
        setupClickListeners();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        nodeNameInput = findViewById(R.id.nodeNameInput);
        apiEndpointInput = findViewById(R.id.apiEndpointInput);
        apiKeyInput = findViewById(R.id.apiKeyInput);
        inputsRecyclerView = findViewById(R.id.inputsRecyclerView);
        outputsRecyclerView = findViewById(R.id.outputsRecyclerView);
        btnAddInput = findViewById(R.id.btnAddInput);
        btnAddOutput = findViewById(R.id.btnAddOutput);
        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnSave);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Создание ноды");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerViews() {
        inputsAdapter = new PortAdapter(PortDirection.INPUT, portName -> {
            portInputs.removeIf(p -> p.getName().equals(portName));
            inputsAdapter.notifyDataSetChanged();
        });
        inputsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        inputsRecyclerView.setAdapter(inputsAdapter);

        outputsAdapter = new PortAdapter(PortDirection.OUTPUT, portName -> {
            portOutputs.removeIf(p -> p.getName().equals(portName));
            outputsAdapter.notifyDataSetChanged();
        });
        outputsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        outputsRecyclerView.setAdapter(outputsAdapter);
    }

    private void setupClickListeners() {
        btnAddInput.setOnClickListener(v -> addPort(PortDirection.INPUT));
        btnAddOutput.setOnClickListener(v -> addPort(PortDirection.OUTPUT));
        btnCancel.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> saveCustomNode());
    }

    private void addPort(PortDirection direction) {
        int size = direction == PortDirection.INPUT ? portInputs.size() : portOutputs.size();
        String portName = "Port_" + size;
        PortItem portItem = new PortItem(portName, PortType.TEXT, direction);

        if (direction == PortDirection.INPUT) {
            portInputs.add(portItem);
            inputsAdapter.notifyDataSetChanged();
        } else {
            portOutputs.add(portItem);
            outputsAdapter.notifyDataSetChanged();
        }
    }

    private void saveCustomNode() {
        String nodeName = getText(nodeNameInput);
        String apiEndpoint = getText(apiEndpointInput);
        String apiKey = getText(apiKeyInput);

        if (nodeName == null || nodeName.isBlank()) {
            nodeNameInput.setError("Введите название");
            return;
        }

        if (apiEndpoint == null || apiEndpoint.isBlank()) {
            apiEndpointInput.setError("Введите API Endpoint");
            return;
        }

        // Создаем кастомную ноду
        CustomNode customNode = new CustomNode(
            String.valueOf(System.currentTimeMillis()),
            nodeName
        );
        customNode.setApiEndpoint(apiEndpoint);
        customNode.setApiKey(apiKey != null ? apiKey : "");

        // Добавляем порты
        for (PortItem port : portInputs) {
            customNode.addInput(port.getName(), port.getType());
        }
        for (PortItem port : portOutputs) {
            customNode.addOutput(port.getName(), port.getType());
        }

        // TODO: Сохранить ноду в хранилище

        Toast.makeText(this, "Нока '" + nodeName + "' сохранена", Toast.LENGTH_SHORT).show();
        finish();
    }

    private String getText(TextInputEditText editText) {
        return editText.getText() != null ? editText.getText().toString().trim() : null;
    }
}
