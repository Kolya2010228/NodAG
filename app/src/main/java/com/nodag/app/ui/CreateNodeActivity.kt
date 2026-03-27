package com.nodag.app.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.nodag.app.R
import com.nodag.app.nodes.CustomNode
import com.nodag.app.nodes.PortDirection
import com.nodag.app.nodes.PortType

/**
 * Экран создания кастомной ноды
 */
class CreateNodeActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var nodeNameInput: TextInputEditText
    private lateinit var apiEndpointInput: TextInputEditText
    private lateinit var apiKeyInput: TextInputEditText
    private lateinit var inputsRecyclerView: RecyclerView
    private lateinit var outputsRecyclerView: RecyclerView
    private lateinit var btnAddInput: MaterialButton
    private lateinit var btnAddOutput: MaterialButton
    private lateinit var btnCancel: MaterialButton
    private lateinit var btnSave: MaterialButton

    private val portInputs = mutableListOf<PortItem>()
    private val portOutputs = mutableListOf<PortItem>()
    private lateinit var inputsAdapter: PortAdapter
    private lateinit var outputsAdapter: PortAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_node)

        initViews()
        setupToolbar()
        setupRecyclerViews()
        setupClickListeners()
    }

    private fun initViews() {
        toolbar = findViewById(R.id.toolbar)
        nodeNameInput = findViewById(R.id.nodeNameInput)
        apiEndpointInput = findViewById(R.id.apiEndpointInput)
        apiKeyInput = findViewById(R.id.apiKeyInput)
        inputsRecyclerView = findViewById(R.id.inputsRecyclerView)
        outputsRecyclerView = findViewById(R.id.outputsRecyclerView)
        btnAddInput = findViewById(R.id.btnAddInput)
        btnAddOutput = findViewById(R.id.btnAddOutput)
        btnCancel = findViewById(R.id.btnCancel)
        btnSave = findViewById(R.id.btnSave)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupRecyclerViews() {
        inputsAdapter = PortAdapter(PortDirection.INPUT) { portName ->
            portInputs.removeAll { it.name == portName }
            inputsAdapter.notifyDataSetChanged()
        }
        inputsRecyclerView.layoutManager = LinearLayoutManager(this)
        inputsRecyclerView.adapter = inputsAdapter

        outputsAdapter = PortAdapter(PortDirection.OUTPUT) { portName ->
            portOutputs.removeAll { it.name == portName }
            outputsAdapter.notifyDataSetChanged()
        }
        outputsRecyclerView.layoutManager = LinearLayoutManager(this)
        outputsRecyclerView.adapter = outputsAdapter
    }

    private fun setupClickListeners() {
        btnAddInput.setOnClickListener {
            addPort(PortDirection.INPUT)
        }

        btnAddOutput.setOnClickListener {
            addPort(PortDirection.OUTPUT)
        }

        btnCancel.setOnClickListener {
            finish()
        }

        btnSave.setOnClickListener {
            saveCustomNode()
        }
    }

    private fun addPort(direction: PortDirection) {
        val portName = "Port_${if (direction == PortDirection.INPUT) portInputs.size else portOutputs.size}"
        val portItem = PortItem(portName, PortType.TEXT, direction)
        
        if (direction == PortDirection.INPUT) {
            portInputs.add(portItem)
            inputsAdapter.notifyDataSetChanged()
        } else {
            portOutputs.add(portItem)
            outputsAdapter.notifyDataSetChanged()
        }
    }

    private fun saveCustomNode() {
        val nodeName = nodeNameInput.text?.toString()?.trim()
        val apiEndpoint = apiEndpointInput.text?.toString()?.trim()
        val apiKey = apiKeyInput.text?.toString()?.trim()

        if (nodeName.isNullOrBlank()) {
            nodeNameInput.error = "Введите название"
            return
        }

        if (apiEndpoint.isNullOrBlank()) {
            apiEndpointInput.error = "Введите API Endpoint"
            return
        }

        // Создаем кастомную ноду
        val customNode = CustomNode(
            id = System.currentTimeMillis().toString(),
            name = nodeName
        )
        customNode.apiEndpoint = apiEndpoint
        customNode.apiKey = apiKey ?: ""

        // Добавляем порты
        portInputs.forEach { port ->
            customNode.addInput(port.name, port.type)
        }
        portOutputs.forEach { port ->
            customNode.addOutput(port.name, port.type)
        }

        // TODO: Сохранить ноду в хранилище

        Toast.makeText(this, "Нока '$nodeName' сохранена", Toast.LENGTH_SHORT).show()
        finish()
    }
}

/**
 * Элемент порта
 */
data class PortItem(
    val name: String,
    val type: PortType,
    val direction: PortDirection
)
