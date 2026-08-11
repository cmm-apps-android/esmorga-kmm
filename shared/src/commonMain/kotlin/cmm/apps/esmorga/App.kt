package cmm.apps.esmorga

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cmm.apps.esmorga.navigation.Navigation
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
                    // TODO: Implement LoginScreen
                }
                composable<Navigation.EventListScreen> {
                    // TODO: Implement EventListScreen
                }
            }
        }
    }
}
