package com.example.banqueteriapaolicias.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.banqueteriapaolicias.ui.screen.CatalogoScreen
import com.example.banqueteriapaolicias.ui.screen.ComprobanteScreen
import com.example.banqueteriapaolicias.ui.screen.CotizacionScreen
import com.example.banqueteriapaolicias.ui.screen.EventoScreen
import com.example.banqueteriapaolicias.ui.screen.LoginScreen
import com.example.banqueteriapaolicias.ui.screen.RegistroScreen

object Routes {
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val CATALOGO = "catalogo"
    const val EVENTO = "evento"
    const val COTIZACION = "cotizacion"
    const val COMPROBANTE = "comprobante"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN,
        enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -it / 3 }) + fadeOut() },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -it / 3 }) + fadeIn() },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) + fadeOut() }
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Routes.REGISTRO) },
                onLoginSuccess = {
                    navController.navigate(Routes.CATALOGO) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.REGISTRO) {
            RegistroScreen(
                onNavigateToLogin = { navController.popBackStack() }
            )
        }
        composable(Routes.CATALOGO) {
            CatalogoScreen(
                onNavigateToEvento = { navController.navigate(Routes.EVENTO) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.EVENTO) {
            EventoScreen(
                onNavigateToCotizacion = { navController.navigate(Routes.COTIZACION) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.COTIZACION) {
            CotizacionScreen(
                onNavigateToComprobante = { navController.navigate(Routes.COMPROBANTE) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.COMPROBANTE) {
            ComprobanteScreen(
                onNavigateToAgenda = {
                    navController.navigate(Routes.CATALOGO) {
                        popUpTo(Routes.CATALOGO) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}