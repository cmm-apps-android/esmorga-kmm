package cmm.apps.esmorga

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalUriHandler
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import cmm.apps.esmorga.screens.errors.EsmorgaErrorScreen
import cmm.apps.esmorga.screens.eventdetails.EventDetailsScreen
import cmm.apps.esmorga.screens.login.LoginScreen
import cmm.apps.esmorga.navigation.Navigation
import cmm.apps.esmorga.screens.registration.RegistrationScreen
import cmm.apps.esmorga.screens.eventlist.EventListScreen
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import cmm.apps.esmorga.screens.welcome.WelcomeScreen
import cmm.apps.esmorga.utils.buildMapUri

@Composable
fun App() {
    EsmorgaTheme {
        Surface {
            val navController: NavHostController = rememberNavController()
            val uriHandler = LocalUriHandler.current
            NavHost(
                navController = navController,
                startDestination = Navigation.WelcomeScreen
            ) {
                composable<Navigation.WelcomeScreen> {
                    WelcomeScreen(
                        onLoginRegisterClicked = {
                            navController.navigate(Navigation.LoginScreen)
                        },
                        onEnterAsGuestClicked = {
                            navController.navigate(Navigation.EventListScreen) {
                                popUpTo(Navigation.WelcomeScreen) { inclusive = true }
                            }
                        }
                    )
                }
                composable<Navigation.LoginScreen> {
                    LoginScreen(
                        onRegisterClicked = {
                            navController.navigate(Navigation.RegistrationScreen)
                        },
                        onLoginSuccess = {
                            navController.navigate(Navigation.EventListScreen) {
                                popUpTo(Navigation.WelcomeScreen) { inclusive = true }
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
                            navController.navigate(Navigation.EventListScreen) {
                                popUpTo(Navigation.WelcomeScreen) { inclusive = true }
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
