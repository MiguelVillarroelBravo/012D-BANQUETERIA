package com.example.banqueteriapaolicias.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.banqueteriapaolicias.ui.screen.AgendaScreen
import com.example.banqueteriapaolicias.ui.screen.CatalogoScreen
import com.example.banqueteriapaolicias.ui.screen.ComprobanteScreen
import com.example.banqueteriapaolicias.ui.screen.CotizacionScreen
import com.example.banqueteriapaolicias.ui.screen.EventoScreen
import com.example.banqueteriapaolicias.ui.screen.LoginScreen
import com.example.banqueteriapaolicias.ui.screen.RegistroScreen

object Routes {
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val EVENTO = "evento"
    const val COTIZACION = "cotizacion"
    const val COMPROBANTE = "comprobante"
    const val AGENDA = "agenda"

    const val CATALOGO = "catalogo"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Routes.REGISTRO) },
                onNavigateToAgenda = { navController.navigate(Routes.CATALOGO) }
            )
        }
        composable(Routes.REGISTRO) {
            RegistroScreen(
                onNavigateToLogin = { navController.popBackStack() }
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
                onNavigateToAgenda = { navController.navigate(Routes.AGENDA) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.AGENDA) {
            AgendaScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.CATALOGO) {
            CatalogoScreen(
                onNavigateToEvento = { navController.navigate(Routes.EVENTO) },
                onBack = { navController.popBackStack() }
            )
        }
    }

}