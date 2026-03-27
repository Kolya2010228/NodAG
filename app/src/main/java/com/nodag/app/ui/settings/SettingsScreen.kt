package com.nodag.app.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.nodag.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit
) {
    var openaiEnabled by remember { mutableStateOf(true) }
    var sdEnabled by remember { mutableStateOf(true) }
    var anthropicEnabled by remember { mutableStateOf(false) }
    var googleEnabled by remember { mutableStateOf(false) }
    
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
                        "Настройки",
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
                // Profile Section
                item {
                    ProfileCard()
                }
                
                // API Keys Section
                item {
                    Text(
                        "API Ключи",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                
                item {
                    ApiKeyItem(
                        name = "OpenAI",
                        status = "sk-••••••••",
                        isEnabled = openaiEnabled,
                        serviceColor = OpenAIColor,
                        onToggle = { openaiEnabled = it }
                    )
                }
                
                item {
                    ApiKeyItem(
                        name = "Stable Diffusion",
                        status = "Настроен",
                        isEnabled = sdEnabled,
                        serviceColor = StableDiffusionColor,
                        onToggle = { sdEnabled = it }
                    )
                }
                
                item {
                    ApiKeyItem(
                        name = "Anthropic",
                        status = "Не настроен",
                        isEnabled = anthropicEnabled,
                        serviceColor = AnthropicColor,
                        onToggle = { anthropicEnabled = it }
                    )
                }
                
                item {
                    ApiKeyItem(
                        name = "Google AI",
                        status = "Не настроен",
                        isEnabled = googleEnabled,
                        serviceColor = GoogleAIColor,
                        onToggle = { googleEnabled = it }
                    )
                }
                
                // App Settings Section
                item {
                    Text(
                        "Приложение",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                
                item {
                    SettingsItem("🎨 Тема", "Тёмная")
                }
                
                item {
                    SettingsItem("🌐 Язык", "Русский")
                }
                
                item {
                    SettingsItem("💾 Экспорт", "JSON, PNG")
                }
                
                item {
                    SettingsItem("ℹ️ О приложении", "v1.0.0")
                }
                
                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
        
        // Bottom Navigation placeholder
        BottomNavPlaceholder()
    }
}

@Composable
fun ProfileCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF16213E)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color(0xFF1F3460), RoundedCornerShape(40.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("👤", fontSize = 40.sp)
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(
                    text = "Пользователь",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "user@nodag.dev",
                    fontSize = 16.sp,
                    color = TextSecondaryColor
                )
            }
        }
    }
}

@Composable
fun ApiKeyItem(
    name: String,
    status: String,
    isEnabled: Boolean,
    serviceColor: Color,
    onToggle: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF16213E)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = serviceColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = status,
                    fontSize = 14.sp,
                    color = if (isEnabled) AccentGreen else TextSecondaryColor
                )
            }
            
            Switch(
                checked = isEnabled,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = AccentGreen,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = DividerColor
                )
            )
        }
    }
}

@Composable
fun SettingsItem(
    title: String,
    value: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF16213E)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                color = Color.White
            )
            Text(
                text = value,
                fontSize = 14.sp,
                color = TextSecondaryColor
            )
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
            BottomNavItemPlaceholder("Ноды")
            BottomNavItemPlaceholder("Настройки", isSelected = true)
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
