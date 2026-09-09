package cmm.esmorga

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import cmm.esmorga.navigation.Navigation
import cmm.esmorga.navigation.NavigationKeys
import cmm.esmorga.screens.changepassword.ChangePasswordScreen
import cmm.esmorga.screens.errors.EsmorgaErrorScreen
import cmm.esmorga.screens.eventdetails.EventDetailsScreen
import cmm.esmorga.screens.explore.ExploreScreen
import cmm.esmorga.screens.home.HomeScreen
import cmm.esmorga.screens.login.LoginScreen
import cmm.esmorga.screens.registration.RegistrationScreen
import cmm.esmorga.utils.buildMapUri
import cmm.esmorga.view.theme.EsmorgaTheme

@Composable
fun App() {
    EsmorgaTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            val navController: NavHostController = rememberNavController()
            val uriHandler = LocalUriHandler.current
            NavHost(
                navController = navController,
                startDestination = Navigation.HomeScreen
            ) {
                composable<Navigation.HomeScreen> { backStackEntry ->
                    HomeScreen(
                        backStackEntry = backStackEntry,
                        onEventClick = { eventId ->
                            navController.navigate(Navigation.EventDetailScreen(eventId))
                        },
                        onNavigateToLogin = {
                            navController.navigate(Navigation.LoginScreen)
                        },
                        onNavigateToChangePassword = {
                            navController.navigate(Navigation.ChangePasswordScreen)
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
                composable<Navigation.ChangePasswordScreen> {
                    ChangePasswordScreen(
                        onBackClicked = {
                            navController.popBackStack()
                        },
                        onChangePasswordSuccess = {
                            navController.getBackStackEntry<Navigation.HomeScreen>().savedStateHandle[NavigationKeys.PASSWORD_CHANGE_SUCCESS] = true
                            navController.popBackStack(Navigation.HomeScreen, inclusive = false)
                        },
                        onChangePasswordError = { error ->
                            navController.navigate(Navigation.FullScreenError(esmorgaErrorScreenArguments = error))
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
                    ExploreScreen(
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
                        },
                        onNavigateToLogin = {
                            navController.navigate(Navigation.LoginScreen)
                        },
                        onNavigateToError = {
                            navController.navigate(Navigation.FullScreenError())
                        }
                    )
                }
            }
        }
    }
}
