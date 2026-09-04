package cmm.esmorga.screens.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cmm.esmorga.designsystem.EsmorgaText
import cmm.esmorga.designsystem.EsmorgaTextStyle
import cmm.esmorga.screens.eventlist.EventListScreen
import cmm.esmorga.screens.myevents.MyEventsScreen
import cmm.esmorga.screens.profile.ProfileScreen
import cmm.esmorga.shared.generated.resources.Res
import cmm.esmorga.shared.generated.resources.bottom_bar_explore
import cmm.esmorga.shared.generated.resources.bottom_bar_myevents
import cmm.esmorga.shared.generated.resources.bottom_bar_myprofile
import cmm.esmorga.shared.generated.resources.event_list_title
import cmm.esmorga.shared.generated.resources.ic_explore
import cmm.esmorga.shared.generated.resources.ic_my_events
import cmm.esmorga.shared.generated.resources.ic_profile
import cmm.esmorga.shared.generated.resources.screen_my_events_title
import cmm.esmorga.utils.screenTopBarInsets
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

enum class HomeTab(val title: StringResource, val icon: DrawableResource) {
    Explore(Res.string.event_list_title, Res.drawable.ic_explore),
    MyEvents(Res.string.screen_my_events_title, Res.drawable.ic_my_events),
    Profile(Res.string.bottom_bar_myprofile, Res.drawable.ic_profile)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onEventClick: (String) -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToChangePassword: () -> Unit
) {
    var selectedTab by rememberSaveable { mutableStateOf(HomeTab.Explore) }
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    EsmorgaText(
                        text = stringResource(selectedTab.title),
                        style = EsmorgaTextStyle.HEADING_1
                    )
                },
                windowInsets = screenTopBarInsets(),
            )
        },
        bottomBar = {
            NavigationBar {
                HomeTab.entries.forEach { tab ->
                    val label = when (tab) {
                        HomeTab.Explore -> stringResource(Res.string.bottom_bar_explore)
                        HomeTab.MyEvents -> stringResource(Res.string.bottom_bar_myevents)
                        HomeTab.Profile -> stringResource(tab.title)
                    }
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        label = { EsmorgaText(label, style = EsmorgaTextStyle.CAPTION) },
                        icon = { Icon(painterResource(tab.icon), contentDescription = label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = colorScheme.primary,
                            selectedTextColor = colorScheme.primary,
                            unselectedIconColor = colorScheme.onSurfaceVariant,
                            unselectedTextColor = colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Crossfade(
            targetState = selectedTab,
            modifier = Modifier.padding(innerPadding)
        ) { tab ->
            when (tab) {
                HomeTab.Explore -> EventListScreen(
                    onEventClick = onEventClick,
                    snackbarHostState = snackbarHostState
                )

                HomeTab.MyEvents -> MyEventsScreen(
                    onNavigateToLogin = onNavigateToLogin,
                    onEventClick = onEventClick
                )

                HomeTab.Profile -> ProfileScreen(
                    onNavigateToLogin = onNavigateToLogin,
                    onNavigateToChangePassword = onNavigateToChangePassword
                )
            }
        }
    }
}
