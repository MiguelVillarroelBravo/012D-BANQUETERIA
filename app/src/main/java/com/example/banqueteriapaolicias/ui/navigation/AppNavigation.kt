package com.example.banqueteriapaolicias.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.banqueteriapaolicias.ui.screen.AgendaScreen
import com.example.banqueteriapaolicias.ui.screen.RegistroScreen
object Routes {
    const val REGISTRO = "registro"
    const val AGENDA = "agenda"
}

@Composable
fun AppNavigation(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.REGISTRO
    ){
        composable(Routes.REGISTRO){
            RegistroScreen(
                onNavigateToAgenda = {navController.navigate(Routes.AGENDA)}
            )
        }
        composable(Routes.AGENDA){
            AgendaScreen(
                onBack = {navController.popBackStack()}
            )
        }
    }
}