package com.dyusov.news.presentation.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.dyusov.news.presentation.screen.settings.SettingsScreen
import com.dyusov.news.presentation.screen.subsriptions.SubscriptionsScreen

@Composable
fun NavRoute() {
    // init backstack with first screen
    val backstack = rememberNavBackStack(Route.MainScreen)

    NavDisplay(
        backStack = backstack,
        entryDecorators = listOf(
            // Add the default decorators for managing scenes and saving state
            rememberSaveableStateHolderNavEntryDecorator(),
            // Then add the view model store decorator
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            // each entry for each screen
            entry<Route.MainScreen> {
                SubscriptionsScreen(
                    onNavigateToSettings = {
                        // Push a key onto the back stack (navigate forward)
                        backstack.add(Route.SettingsScreen)
                    }
                )
            }
            entry<Route.SettingsScreen> {
                SettingsScreen(
                    onBackClick = {
                        // Pop a key off the back stack (navigate back)
                        backstack.removeLastOrNull()
                    }
                )
            }
        },
        // modify transitions between screens
        transitionSpec = {
            // Slide in from right when navigating forward
            slideInHorizontally(initialOffsetX = { it }) togetherWith slideOutHorizontally(
                targetOffsetX = { -it })
        },
        popTransitionSpec = {
            // Slide in from left when navigating back
            slideInHorizontally(initialOffsetX = { -it }) togetherWith slideOutHorizontally(
                targetOffsetX = { it })
        },
        predictivePopTransitionSpec = {
            // Slide in from left when navigating back
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
        }
    )
}