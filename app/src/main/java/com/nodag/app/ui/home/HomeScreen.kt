package com.nodag.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nodag.app.data.model.Workflow
import com.nodag.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToEditor: (String) -> Unit,
    onNavigateToNodeCreator: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    
    // Пример данных (потом заменить на реальные из БД)
    val workflows = remember {
        listOf(
            Workflow(
                name = "GPT Pipeline",
                description = "3 ноды",
                nodes = listOf(),
                lastOpenedAt = System.currentTimeMillis() - 2 * 60 * 60 * 1000
            ),
            Workflow(
                name = "Image Generator",
                description = "5 нод",
                nodes = listOf(),
                lastOpenedAt = System.currentTimeMillis() - 24 * 60 * 60 * 1000
            ),
            Workflow(
                name = "Text Analyzer",
                description = "4 ноды",
                nodes = listOf(),
                lastOpenedAt = System.currentTimeMillis() - 3 * 24 * 60 * 60 * 1000
            ),
            Workflow(
                name = "Search + Summarize",
                description = "6 нод",
                nodes = listOf(),
                lastOpenedAt = System.currentTimeMillis() - 6 * 24 * 60 * 60 * 1000
            )
        )
    }
    
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF1A1A2E))) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Toolbar
            TopAppBar(
                title = {
                    Text(
                        "NodAG",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                actions = {
                    IconButton(onClick = onNavigateToNodeCreator) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Create",
                            tint = Color(0xFFE94560),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF16213E)
                )
            )
            
            // Content
            Text(
                text = "Мои воркфлоу",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )
            
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(workflows) { workflow ->
                    WorkflowCard(
                        workflow = workflow,
                        onClick = { onNavigateToEditor(workflow.id) }
                    )
                }
                
                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
        
        // FAB
        FloatingActionButton(
            onClick = onNavigateToNodeCreator,
            containerColor = Color(0xFFE94560),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .offset(y = (-80).dp)
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Add",
                modifier = Modifier.size(32.dp)
            )
        }
        
        // Bottom Navigation
        BottomNavigationBar(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            onNavigateToSettings = onNavigateToSettings
        )
    }
}

@Composable
fun WorkflowCard(
    workflow: Workflow,
    onClick: () -> Unit
) {
    val accentColor = when (workflow.name) {
        "GPT Pipeline" -> AccentBlue
        "Image Generator" -> AccentPurple
        "Text Analyzer" -> AccentOrange
        else -> AccentGreen
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF16213E)
        )
    ) {
        Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            // Accent bar
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(accentColor, RoundedCornerShape(12.dp))
            )
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = workflow.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${workflow.description} • ${workflow.getFormattedLastOpened()}",
                    fontSize = 14.sp,
                    color = TextSecondaryColor
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    onNavigateToSettings: () -> Unit
) {
    BottomAppBar(
        containerColor = Color(0xFF16213E),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(70.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Home
            BottomNavItem(
                icon = if (selectedTab == 0) Icons.Filled.Home else Icons.Outlined.Home,
                label = "Главная",
                isSelected = selectedTab == 0,
                onClick = { onTabSelected(0) }
            )
            
            // Nodes
            BottomNavItem(
                icon = if (selectedTab == 1) Icons.Filled.Add else Icons.Outlined.Add,
                label = "Ноды",
                isSelected = selectedTab == 1,
                onClick = { onTabSelected(1) }
            )
            
            // Settings
            BottomNavItem(
                icon = if (selectedTab == 2) Icons.Filled.Settings else Icons.Outlined.Settings,
                label = "Настройки",
                isSelected = selectedTab == 2,
                onClick = {
                    onTabSelected(2)
                    onNavigateToSettings()
                }
            )
        }
    }
}

@Composable
fun BottomNavItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick).padding(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) Color.White else TextSecondaryColor,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = if (isSelected) Color.White else TextSecondaryColor
        )
    }
}
