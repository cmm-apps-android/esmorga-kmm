package cmm.esmorga.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import cmm.esmorga.screens.createevent.CreateEventStep1Screen
import cmm.esmorga.screens.createevent.CreateEventStep2Screen
import cmm.esmorga.screens.createevent.CreateEventStep3Screen
import cmm.esmorga.screens.createevent.CreateEventStep4Screen
import cmm.esmorga.screens.createevent.CreateEventStep5Screen

fun NavGraphBuilder.createEventGraph(navController: NavHostController) {
    navigation<Navigation.CreateEventFlow>(
        startDestination = Navigation.CreateEventStep1
    ) {
        composable<Navigation.CreateEventStep1> {
            CreateEventStep1Screen(
                onNavigateToStep2 = { navController.navigate(Navigation.CreateEventStep2) },
                onBackPressed = { navController.popBackStack() }
            )
        }
        composable<Navigation.CreateEventStep2> {
            CreateEventStep2Screen(
                onNext = { navController.navigate(Navigation.CreateEventStep3) },
                onBackPressed = { navController.popBackStack() }
            )
        }
        composable<Navigation.CreateEventStep3> {
            CreateEventStep3Screen(
                onNext = { navController.navigate(Navigation.CreateEventStep4) },
                onBackPressed = { navController.popBackStack() }
            )
        }
        composable<Navigation.CreateEventStep4> {
            CreateEventStep4Screen(
                onNext = { navController.navigate(Navigation.CreateEventStep5) },
                onBackPressed = { navController.popBackStack() }
            )
        }
        composable<Navigation.CreateEventStep5> {
            CreateEventStep5Screen(
                onNext = {
                    navController.navigate(Navigation.HomeScreen) {
                        popUpTo<Navigation.CreateEventFlow> { inclusive = true }
                    }
                },
                onBackPressed = { navController.popBackStack() }
            )
        }
    }
}
