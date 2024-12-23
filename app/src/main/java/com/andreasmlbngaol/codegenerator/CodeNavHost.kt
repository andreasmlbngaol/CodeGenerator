package com.andreasmlbngaol.codegenerator

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import androidx.navigation.compose.NavHost
import com.andreasmlbngaol.codegenerator.views.code.CodeScreen

@Serializable object Home

@Composable
fun CodeNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Home
    ) {
        composable<Home> {
            CodeScreen()
        }
    }
}