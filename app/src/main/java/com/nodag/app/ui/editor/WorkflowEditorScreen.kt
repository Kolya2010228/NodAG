package com.nodag.app.ui.editor

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nodag.app.data.model.*
import com.nodag.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkflowEditorScreen(
    workflowId: String,
    onNavigateBack: () -> Unit
) {
    var nodes by remember {
        mutableStateOf(
            listOf(
                Node(
                    name = "File Input",
                    type = NodeType.INPUT,
                    position = NodePosition(100f, 150f),
                    config = NodeConfig(),
                    outputs = listOf(
                        NodePort(name = "file_path", dataType = DataType.STRING),
                        NodePort(name = "content", dataType = DataType.STRING)
                    )
                ),
                Node(
                    name = "OpenAI GPT-4",
                    type = NodeType.PROCESS,
                    serviceType = ServiceType.OPENAI,
                    position = NodePosition(500f, 150f),
                    config = NodeConfig(),
                    inputs = listOf(
                        NodePort(name = "prompt", dataType = DataType.STRING),
                        NodePort(name = "context", dataType = DataType.STRING)
                    ),
                    outputs = listOf(
                        NodePort(name = "response", dataType = DataType.STRING)
                    )
                ),
                Node(
                    name = "Text Output",
                    type = NodeType.OUTPUT,
                    position = NodePosition(300f, 500f),
                    config = NodeConfig(),
                    inputs = listOf(
                        NodePort(name = "text", dataType = DataType.STRING),
                        NodePort(name = "format", dataType = DataType.STRING)
                    )
                )
            )
        )
    }
    
    var connections by remember {
        mutableStateOf(
            listOf(
                NodeConnection(
                    fromNodeId = "0",
                    fromPortId = "1",
                    toNodeId = "1",
                    toPortId = "0"
                ),
                NodeConnection(
                    fromNodeId = "1",
                    fromPortId = "0",
                    toNodeId = "2",
                    toPortId = "0"
                )
            )
        )
    }
    
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF1A1A2E))) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Toolbar
            TopAppBar(
                title = {
                    Text(
                        "Редактор",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    TextButton(onClick = { /* Save */ }) {
                        Text("Сохранить", color = Color(0xFFE94560))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF16213E)
                )
            )
            
            // Canvas with grid
            WorkflowCanvas(
                nodes = nodes,
                connections = connections,
                onNodeMoved = { nodeId, newPosition ->
                    nodes = nodes.map { node ->
                        if (node.id == nodeId) node.copy(position = newPosition) else node
                    }
                }
            )
        }
        
        // Bottom tool panel
        BottomToolPanel(
            onAddNode = { nodeType ->
                val newNode = when (nodeType) {
                    "input" -> Node(
                        name = "File Input",
                        type = NodeType.INPUT,
                        position = NodePosition(100f, 150f),
                        config = NodeConfig(),
                        outputs = listOf(NodePort(name = "file_path", dataType = DataType.STRING))
                    )
                    "ai" -> Node(
                        name = "OpenAI GPT-4",
                        type = NodeType.PROCESS,
                        serviceType = ServiceType.OPENAI,
                        position = NodePosition(300f, 300f),
                        config = NodeConfig(),
                        inputs = listOf(NodePort(name = "prompt", dataType = DataType.STRING)),
                        outputs = listOf(NodePort(name = "response", dataType = DataType.STRING))
                    )
                    "output" -> Node(
                        name = "Text Output",
                        type = NodeType.OUTPUT,
                        position = NodePosition(500f, 450f),
                        config = NodeConfig(),
                        inputs = listOf(NodePort(name = "text", dataType = DataType.STRING))
                    )
                    else -> return@BottomToolPanel
                }
                nodes = nodes + newNode
            }
        )
    }
}

@Composable
fun WorkflowCanvas(
    nodes: List<Node>,
    connections: List<NodeConnection>,
    onNodeMoved: (String, NodePosition) -> Unit
) {
    var dragOffset by remember { mutableStateOf<Offset?>(null) }
    var draggingNodeId by remember { mutableStateOf<String?>(null) }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A2E))
    ) {
        // Grid
        Canvas(modifier = Modifier.fillMaxSize()) {
            val gridSize = 100f
            val stroke = Stroke(width = 2f)
            
            for (x in 0..size.width.toInt() step gridSize.toInt()) {
                drawLine(
                    color = DividerColor,
                    start = Offset(x.toFloat(), 0f),
                    end = Offset(x.toFloat(), size.height),
                    strokeWidth = 2f
                )
            }
            for (y in 0..size.height.toInt() step gridSize.toInt()) {
                drawLine(
                    color = DividerColor,
                    start = Offset(0f, y.toFloat()),
                    end = Offset(size.width, y.toFloat()),
                    strokeWidth = 2f
                )
            }
        }
        
        // Connection lines
        Canvas(modifier = Modifier.fillMaxSize()) {
            connections.forEach { connection ->
                val fromNode = nodes.find { it.id == connection.fromNodeId }
                val toNode = nodes.find { it.id == connection.toNodeId }
                
                if (fromNode != null && toNode != null) {
                    val fromOffset = Offset(
                        fromNode.position.x + 300f,
                        fromNode.position.y + 150f
                    )
                    val toOffset = Offset(
                        toNode.position.x,
                        toNode.position.y + 150f
                    )
                    
                    drawLine(
                        color = Color(0xFFE94560),
                        start = fromOffset,
                        end = toOffset,
                        strokeWidth = 4f
                    )
                }
            }
        }
        
        // Nodes
        nodes.forEach { node ->
            NodeView(
                node = node,
                onNodeDragged = { offset ->
                    draggingNodeId = node.id
                    dragOffset = offset
                },
                onDragEnd = {
                    if (draggingNodeId != null && dragOffset != null) {
                        onNodeMoved(
                            draggingNodeId!!,
                            NodePosition(dragOffset!!.x, dragOffset!!.y)
                        )
                    }
                    draggingNodeId = null
                    dragOffset = null
                }
            )
        }
    }
}

@Composable
fun NodeView(
    node: Node,
    onNodeDragged: (Offset) -> Unit,
    onDragEnd: () -> Unit
) {
    val nodeColor = when (node.type) {
        NodeType.INPUT -> NodeInputColor
        NodeType.PROCESS -> NodeProcessColor
        NodeType.OUTPUT -> NodeOutputColor
        NodeType.LOGIC -> NodeLogicColor
        NodeType.UTIL -> NodeUtilColor
    }
    
    Box(
        modifier = Modifier
            .offset {
                (node.position.x.dp).roundToInt().toDp() to 
                (node.position.y.dp).roundToInt().toDp()
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = { onDragEnd() },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        onNodeDragged(
                            Offset(
                                node.position.x + dragAmount.x,
                                node.position.y + dragAmount.y
                            )
                        )
                    }
                )
            }
            .width(280.dp)
            .height(180.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = nodeColor
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = node.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Inputs
                node.inputs.forEach { input ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(AccentBlue, RoundedCornerShape(8.dp))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = input.name,
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
                
                // Outputs
                node.outputs.forEach { output ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = output.name,
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(AccentGreen, RoundedCornerShape(8.dp))
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BottomToolPanel(
    onAddNode: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .background(Color(0xFF16213E))
            .padding(vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ToolButton("📁 Input", { onAddNode("input") })
            ToolButton("🤖 AI", { onAddNode("ai") })
            ToolButton("📤 Output", { onAddNode("output") })
            ToolButton("⚡ Logic", { })
            ToolButton("🔧 Util", { })
        }
    }
}

@Composable
fun ToolButton(
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.White
        )
    }
}
