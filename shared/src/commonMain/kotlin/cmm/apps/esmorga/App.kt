package cmm.apps.esmorga

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cmm.apps.esmorga.login.LoginScreen
import cmm.apps.esmorga.navigation.Navigation
import cmm.apps.esmorga.registration.RegistrationScreen
import cmm.apps.esmorga.eventlist.EventListScreen
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import cmm.apps.esmorga.welcome.WelcomeScreen

@Composable
fun App() {
    EsmorgaTheme {
        Surface {
            val navController: NavHostController = rememberNavController()
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
                            navController.navigate(Navigation.EventListScreen)
                        }
                    )
                }
                composable<Navigation.LoginScreen> {
                    LoginScreen(
                        onRegisterClicked = {
                            navController.navigate(Navigation.RegistrationScreen)
                        },
                        onLoginSuccess = {
                            navController.navigate(Navigation.EventListScreen)
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
                            navController.navigate(Navigation.EventListScreen)
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
                    // TODO: Implement FullScreenError or use a generic one
                }
                composable<Navigation.EventListScreen> {
                    EventListScreen(
                        onEventClick = { eventId ->
                            navController.navigate(Navigation.EventDetailScreen(eventId))
                        }
                    )
                }
                composable<Navigation.EventDetailScreen> { backStackEntry ->
                    // TODO: Implement EventDetailScreen
                }
            }
        }
    }
}
