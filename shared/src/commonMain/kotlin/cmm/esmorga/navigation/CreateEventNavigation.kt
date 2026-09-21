package cmm.esmorga.navigation

import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import cmm.esmorga.screens.createevent.CreateEventStep1Screen
import cmm.esmorga.screens.createevent.CreateEventStep2Screen
import cmm.esmorga.screens.createevent.CreateEventStep3Screen
import cmm.esmorga.viewmodel.createevent.CreateEventViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.createEventGraph(navController: NavHostController) {
    navigation<Navigation.CreateEventFlow>(
        startDestination = Navigation.CreateEventStep1
    ) {
        composable<Navigation.CreateEventStep1> {
            val cevm: CreateEventViewModel = koinViewModel(
                viewModelStoreOwner = remember { navController.getBackStackEntry<Navigation.CreateEventFlow>() }
            )
            CreateEventStep1Screen(
                cevm = cevm,
                onNavigateToStep2 = { navController.navigate(Navigation.CreateEventStep2) },
                onBackPressed = { navController.popBackStack() }
            )
        }
        composable<Navigation.CreateEventStep2> {
            val cevm: CreateEventViewModel = koinViewModel(
                viewModelStoreOwner = remember { navController.getBackStackEntry<Navigation.CreateEventFlow>() }
            )
            CreateEventStep2Screen(
                cevm = cevm,
                onNext = { navController.navigate(Navigation.CreateEventStep3) },
                onBackPressed = { navController.popBackStack() }
            )
        }
        composable<Navigation.CreateEventStep3> {
            val cevm: CreateEventViewModel = koinViewModel(
                viewModelStoreOwner = remember { navController.getBackStackEntry<Navigation.CreateEventFlow>() }
            )
            CreateEventStep3Screen(
                cevm = cevm,
                onNext = {
                    // Logic for completing the flow will be added later
                },
                onBackPressed = { navController.popBackStack() }
            )
        }
    }
}
