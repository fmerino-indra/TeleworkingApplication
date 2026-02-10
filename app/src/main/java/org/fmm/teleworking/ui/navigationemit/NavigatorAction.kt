package org.fmm.teleworking.ui.navigation

import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.ComposeNavigator

sealed interface NavigatorAction {
    data class NavigateTo(
        val destination: ComposeNavigator.Destination,
        val navOptions: NavOptionsBuilder.() -> Unit
    ): NavigatorAction
    data object NavigateUp: NavigatorAction

    data class NavigateWithResult(
        val key: String,
        val result: Any
    ): NavigatorAction
}