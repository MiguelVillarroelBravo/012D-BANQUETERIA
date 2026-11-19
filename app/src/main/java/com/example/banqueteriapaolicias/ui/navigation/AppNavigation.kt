package com.example.banqueteriapaolicias.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.banqueteriapaolicias.data.session.SessionManager
import com.example.banqueteriapaolicias.model.Roles
import com.example.banqueteriapaolicias.ui.screen.AdminCatalogoScreen
import com.example.banqueteriapaolicias.ui.screen.AdminHomeScreen
import com.example.banqueteriapaolicias.ui.screen.CatalogoScreen
import com.example.banqueteriapaolicias.ui.screen.ComprobanteScreen
import com.example.banqueteriapaolicias.ui.screen.CotizacionScreen
import com.example.banqueteriapaolicias.ui.screen.EventoScreen
import com.example.banqueteriapaolicias.ui.screen.LoginScreen
import com.example.banqueteriapaolicias.ui.screen.PlanificacionScreen
import com.example.banqueteriapaolicias.ui.screen.RegistroScreen
import com.example.banqueteriapaolicias.ui.viewmodel.ReservaSharedViewModel
import com.example.banqueteriapaolicias.ui.viewmodel.ReservasViewModel
import com.example.banqueteriapaolicias.ui.screen.MisReservasScreen

object Routes {
    const val LOGIN = "login"
    const val REGISTRO = "registro"

    // Rol CLIENTE
    const val CATALOGO = "catalogo"
    const val EVENTO = "evento"
    const val COTIZACION = "cotizacion"
    const val COMPROBANTE = "comprobante"
    const val MIS_RESERVAS = "mis_reservas"

    const val ADMIN_HOME = "admin_home"
    const val ADMIN_CATALOGO = "admin_catalogo"
    const val PLANIFICACION = "planificacion"
}

@Composable
private fun SoloRol(
    rolRequerido: String,
    session: SessionManager,
    onDenegado: () -> Unit,
    content: @Composable () -> Unit
) {
    if (session.getRol() == rolRequerido) {
        content()
    } else {
        LaunchedEffect(Unit) { onDenegado() }
    }
}

@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val session = remember { SessionManager(context) }
    val navController = rememberNavController()
    val sharedViewModel: ReservaSharedViewModel = viewModel()
    val reservasViewModel: ReservasViewModel = viewModel()

    // Si ya hay sesión guardada, entra directo a la pantalla de su rol
    val startDestination = when (session.getRol()) {
        Roles.ADMIN -> Routes.ADMIN_HOME
        Roles.CLIENTE -> Routes.CATALOGO
        else -> Routes.LOGIN
    }

    val cerrarSesion: () -> Unit = {
        session.clear()
        navController.navigate(Routes.LOGIN) {
            popUpTo(0) { inclusive = true }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -it / 3 }) + fadeOut() },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -it / 3 }) + fadeIn() },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) + fadeOut() }
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Routes.REGISTRO) },
                onLoginSuccess = { rol ->
                    val destino = if (rol == Roles.ADMIN) Routes.ADMIN_HOME else Routes.CATALOGO
                    navController.navigate(destino) {
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
            SoloRol(Roles.CLIENTE, session, cerrarSesion) {
                CatalogoScreen(
                    onNavigateToEvento = { nombre, precioUnitario ->
                        sharedViewModel.guardarServicioSeleccionado(nombre, precioUnitario)
                        navController.navigate(Routes.EVENTO)
                    },
                    onBack = { navController.popBackStack() },
                    onLogout = cerrarSesion
                )
            }
        }

        composable(Routes.EVENTO) {
            SoloRol(Roles.CLIENTE, session, cerrarSesion) {
                EventoScreen(
                    onNavigateToCotizacion = { cantidadAsistentes ->
                        sharedViewModel.guardarCantidadAsistentes(cantidadAsistentes)
                        navController.navigate(Routes.COTIZACION)
                    },
                    onBack = { navController.popBackStack() },
                    sharedViewModel = sharedViewModel
                )
            }
        }

        composable(Routes.COTIZACION) {
            SoloRol(Roles.CLIENTE, session, cerrarSesion) {
                CotizacionScreen(
                    menuInicial = sharedViewModel.servicioNombre,
                    precioInicial = sharedViewModel.servicioPrecioUnitario,
                    comensalesIniciales = sharedViewModel.cantidadAsistentes,
                    onNavigateToComprobante = { montoAbono ->
                        sharedViewModel.guardarMontoAbono(montoAbono)
                        navController.navigate(Routes.COMPROBANTE)
                    },
                    onBack = { navController.popBackStack() },
                    sharedViewModel = sharedViewModel
                )
            }
        }

        composable(Routes.COMPROBANTE) {
            SoloRol(Roles.CLIENTE, session, cerrarSesion) {
                ComprobanteScreen(
                    montoAbonoCincuentaPorCiento = sharedViewModel.montoAbonoCincuentaPorCiento,
                    onNavigateToAgenda = {
                        navController.navigate(Routes.CATALOGO) {
                            popUpTo(Routes.CATALOGO) { inclusive = true }
                        }
                    },
                    onBack = { navController.popBackStack() },
                    sharedViewModel = sharedViewModel,
                    reservasViewModel = reservasViewModel
                )
            }
        }

        composable(Routes.ADMIN_HOME) {
            SoloRol(Roles.ADMIN, session, cerrarSesion) {
                AdminHomeScreen(
                    onNavigateToPlanificacion = { navController.navigate(Routes.PLANIFICACION) },
                    onNavigateToAdminCatalogo = { navController.navigate(Routes.ADMIN_CATALOGO) },
                    onLogout = cerrarSesion
                )
            }
        }

        composable(Routes.ADMIN_CATALOGO) {
            SoloRol(Roles.ADMIN, session, cerrarSesion) {
                AdminCatalogoScreen(onBack = { navController.popBackStack() })
            }
        }

        composable(Routes.PLANIFICACION) {
            SoloRol(Roles.ADMIN, session, cerrarSesion) {
                PlanificacionScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}
