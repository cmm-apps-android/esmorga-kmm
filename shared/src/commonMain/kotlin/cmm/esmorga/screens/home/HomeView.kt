package cmm.esmorga.screens.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cmm.esmorga.designsystem.EsmorgaText
import cmm.esmorga.designsystem.EsmorgaTextStyle
import cmm.esmorga.screens.eventlist.EventListScreen
import cmm.esmorga.screens.myevents.MyEventsScreen
import cmm.esmorga.shared.generated.resources.Res
import cmm.esmorga.shared.generated.resources.bottom_bar_explore
import cmm.esmorga.shared.generated.resources.bottom_bar_myevents
import cmm.esmorga.shared.generated.resources.bottom_bar_myprofile
import cmm.esmorga.shared.generated.resources.ic_explore
import cmm.esmorga.shared.generated.resources.ic_my_events
import cmm.esmorga.shared.generated.resources.ic_profile
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

enum class HomeTab(val title: StringResource, val icon: DrawableResource) {
    Explore(Res.string.bottom_bar_explore, Res.drawable.ic_explore),
    MyEvents(Res.string.bottom_bar_myevents, Res.drawable.ic_my_events),
    Profile(Res.string.bottom_bar_myprofile, Res.drawable.ic_profile)
}

@Composable
fun HomeScreen(onEventClick: (String) -> Unit, onNavigateToLogin: () -> Unit) {
    var selectedTab by rememberSaveable { mutableStateOf(HomeTab.Explore) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                HomeTab.entries.forEach { tab ->
                    val label = stringResource(tab.title)
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        label = { EsmorgaText(label, style = EsmorgaTextStyle.CAPTION) },
                        icon = { Icon(painterResource(tab.icon), contentDescription = label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = colorScheme.primary,
                            selectedTextColor = colorScheme.primary,
                            unselectedIconColor = colorScheme.onSurfaceVariant,
                            unselectedTextColor = colorScheme.onSurfaceVariant,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Crossfade(
            targetState = selectedTab,
            modifier = Modifier.padding(innerPadding)
        ) { tab ->
            when (tab) {
                HomeTab.Explore -> EventListScreen(onEventClick = onEventClick)
                HomeTab.MyEvents -> MyEventsScreen(
                    onNavigateToLogin = onNavigateToLogin,
                    onEventClick = onEventClick
                )
                HomeTab.Profile -> PlaceholderScreen(stringResource(Res.string.bottom_bar_myprofile))
            }
        }
    }
}

@Composable
fun PlaceholderScreen(title: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text(text = title)
    }
}
