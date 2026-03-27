package com.nodag.app.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Editor : Screen("editor/{workflowId}") {
        fun createRoute(workflowId: String) = "editor/$workflowId"
    }
    object NodeCreator : Screen("node_creator")
    object Settings : Screen("settings")
}
