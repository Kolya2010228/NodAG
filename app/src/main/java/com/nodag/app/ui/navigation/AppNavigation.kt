package com.nodag.app.ui.navigation

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nodag.app.ui.editor.WorkflowEditorScreen
import com.nodag.app.ui.home.HomeScreen
import com.nodag.app.ui.nodecreator.NodeCreatorScreen
import com.nodag.app.ui.settings.SettingsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToEditor = { workflowId ->
                    navController.navigate(Screen.Editor.createRoute(workflowId))
                },
                onNavigateToNodeCreator = {
                    navController.navigate(Screen.NodeCreator.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }
        
        composable(
            route = Screen.Editor.route,
            arguments = listOf(
                androidx.navigation.navArgument("workflowId") {
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val workflowId = backStackEntry.arguments?.getString("workflowId") ?: ""
            WorkflowEditorScreen(
                workflowId = workflowId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.NodeCreator.route) {
            NodeCreatorScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
