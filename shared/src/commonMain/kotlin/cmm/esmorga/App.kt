package cmm.esmorga

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalUriHandler
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import cmm.esmorga.screens.errors.EsmorgaErrorScreen
import cmm.esmorga.screens.eventdetails.EventDetailsScreen
import cmm.esmorga.screens.login.LoginScreen
import cmm.esmorga.screens.home.HomeScreen
import cmm.esmorga.navigation.Navigation
import cmm.esmorga.screens.registration.RegistrationScreen
import cmm.esmorga.screens.eventlist.EventListScreen
import cmm.esmorga.view.theme.EsmorgaTheme
import cmm.esmorga.utils.buildMapUri

@Composable
fun App() {
    EsmorgaTheme {
        Surface {
            val navController: NavHostController = rememberNavController()
            val uriHandler = LocalUriHandler.current
            NavHost(
                navController = navController,
                startDestination = Navigation.HomeScreen
            ) {
                composable<Navigation.HomeScreen> {
                    HomeScreen(
                        onEventClick = { eventId ->
                            navController.navigate(Navigation.EventDetailScreen(eventId))
                        },
                        onNavigateToLogin = {
                            navController.navigate(Navigation.LoginScreen)
                        }
                    )
                }

                composable<Navigation.LoginScreen> {
                    LoginScreen(
                        onRegisterClicked = {
                            navController.navigate(Navigation.RegistrationScreen)
                        },
                        onLoginSuccess = {
                            navController.navigate(Navigation.HomeScreen) {
                                popUpTo(Navigation.HomeScreen) { inclusive = true }
                            }
                        },
                        onLoginError = { error ->
                            navController.navigate(Navigation.FullScreenError(esmorgaErrorScreenArguments = error))
                        },
                        onBackClicked = {
                            navController.popBackStack()
                        }
                    )
                }
                composable<Navigation.RegistrationScreen> {
                    RegistrationScreen(
                        onRegistrationSuccess = {
                            navController.navigate(Navigation.HomeScreen) {
                                popUpTo(Navigation.HomeScreen) { inclusive = true }
                            }
                        },
                        onRegistrationError = { error ->
                            navController.navigate(Navigation.FullScreenError(esmorgaErrorScreenArguments = error))
                        },
                        onBackClicked = {
                            navController.popBackStack()
                        }
                    )
                }
                composable<Navigation.FullScreenError> { backStackEntry ->
                    EsmorgaErrorScreen(
                        esmorgaErrorScreenArguments = backStackEntry.toRoute<Navigation.FullScreenError>().esmorgaErrorScreenArguments,
                        onButtonPressed = {
                            navController.popBackStack()
                        }
                    )
                }
                composable<Navigation.EventListScreen> {
                    EventListScreen(
                        onEventClick = { eventId ->
                            navController.navigate(Navigation.EventDetailScreen(eventId))
                        }
                    )
                }
                composable<Navigation.EventDetailScreen> { backStackEntry ->
                    EventDetailsScreen(
                        eventId = backStackEntry.toRoute<Navigation.EventDetailScreen>().eventId,
                        onBackPressed = { navController.popBackStack() },
                        onNavigateToLocation = { lat, lng ->
                            uriHandler.openUri(buildMapUri(lat = lat, lng = lng))
                        }
                    )
                }
            }
        }
    }
}
