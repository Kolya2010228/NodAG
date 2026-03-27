package com.nodag.app.ui.nodecreator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nodag.app.data.model.DataType
import com.nodag.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NodeCreatorScreen(
    onNavigateBack: () -> Unit
) {
    var nodeName by remember { mutableStateOf("My Custom Node") }
    var apiEndpoint by remember { mutableStateOf("https://api.example.com/v1/...") }
    var apiKey by remember { mutableStateOf("sk-••••••••••••••••") }
    
    var inputs by remember {
        mutableStateOf(
            listOf(
                NodePortItem("prompt", DataType.STRING, "Текст запроса"),
                NodePortItem("temperature", DataType.NUMBER, "0.0 - 1.0"),
                NodePortItem("max_tokens", DataType.NUMBER, "Максимум токенов")
            )
        )
    }
    
    var outputs by remember {
        mutableStateOf(
            listOf(
                NodePortItem("response", DataType.STRING, "Ответ от API"),
                NodePortItem("usage", DataType.OBJECT, "Инфо о токенах")
            )
        )
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A2E))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Toolbar
            TopAppBar(
                title = {
                    Text(
                        "Создать ноду",
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF16213E)
                )
            )
            
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Node Name
                item {
                    Column {
                        Text(
                            "Название ноды",
                            fontSize = 14.sp,
                            color = TextSecondaryColor
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = nodeName,
                            onValueChange = { nodeName = it },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFE94560),
                                unfocusedBorderColor = Color(0xFF1F3460),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = Color(0xFFE94560)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                    }
                }
                
                // API Endpoint
                item {
                    Column {
                        Text(
                            "API Endpoint",
                            fontSize = 14.sp,
                            color = TextSecondaryColor
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = apiEndpoint,
                            onValueChange = { apiEndpoint = it },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFE94560),
                                unfocusedBorderColor = Color(0xFF1F3460),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = Color(0xFFE94560)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                    }
                }
                
                // API Key
                item {
                    Column {
                        Text(
                            "API Key",
                            fontSize = 14.sp,
                            color = TextSecondaryColor
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = apiKey,
                            onValueChange = { apiKey = it },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFE94560),
                                unfocusedBorderColor = Color(0xFF1F3460),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = Color(0xFFE94560)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                    }
                }
                
                // Inputs Section
                item {
                    Text(
                        "Входы",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                
                items(inputs) { input ->
                    PortItem(
                        port = input,
                        isInput = true,
                        onDelete = { inputs = inputs.filter { it != input } }
                    )
                }
                
                item {
                    Button(
                        onClick = {
                            inputs = inputs + NodePortItem(
                                "new_input",
                                DataType.STRING,
                                "Описание"
                            )
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1F3460)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("+ Добавить вход", color = Color(0xFFE94560))
                    }
                }
                
                // Outputs Section
                item {
                    Text(
                        "Выходы",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                
                items(outputs) { output ->
                    PortItem(
                        port = output,
                        isInput = false,
                        onDelete = { outputs = outputs.filter { it != output } }
                    )
                }
                
                item {
                    Button(
                        onClick = {
                            outputs = outputs + NodePortItem(
                                "new_output",
                                DataType.STRING,
                                "Описание"
                            )
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1F3460)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("+ Добавить выход", color = Color(0xFFE94560))
                    }
                }
                
                // Save Button
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { /* Save node */ },
                        modifier = Modifier.fillMaxWidth().height(60.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE94560)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            "Сохранить ноду",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
        
        // Bottom Navigation placeholder
        BottomNavPlaceholder()
    }
}

data class NodePortItem(
    val name: String,
    val dataType: DataType,
    val description: String
)

@Composable
fun PortItem(
    port: NodePortItem,
    isInput: Boolean,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1F3460)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Port indicator
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        if (isInput) AccentBlue else AccentGreen,
                        RoundedCornerShape(12.dp)
                    )
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = port.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "${port.dataType.name} • ${port.description}",
                    fontSize = 12.sp,
                    color = TextSecondaryColor
                )
            }
            
            IconButton(onClick = onDelete) {
                Text("×", color = Color(0xFFE94560), fontSize = 24.sp)
            }
        }
    }
}

@Composable
fun BottomNavPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .background(Color(0xFF16213E))
            .height(80.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItemPlaceholder("Главная")
            BottomNavItemPlaceholder("Ноды", isSelected = true)
            BottomNavItemPlaceholder("Настройки")
        }
    }
}

@Composable
fun BottomNavItemPlaceholder(
    label: String,
    isSelected: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(
                    if (isSelected) Color.White else TextSecondaryColor,
                    RoundedCornerShape(4.dp)
                )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = if (isSelected) Color.White else TextSecondaryColor
        )
    }
}
