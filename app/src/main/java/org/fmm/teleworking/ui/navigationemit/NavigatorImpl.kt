package org.fmm.teleworking.ui.navigation

import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.ComposeNavigator.Destination
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Singleton

@Singleton
class NavigatorImpl(
    override val startDestination: Destination
): Navigator {
    private val _navigatorActions: Channel<NavigatorAction> = Channel()

    override val navigatorActions: Flow<NavigatorAction>
        get() = _navigatorActions.receiveAsFlow()

    override suspend fun navigate(
        destination: Destination,
        navOptions: NavOptionsBuilder.() -> Unit
    ) {
        _navigatorActions.send(NavigatorAction.NavigateTo(destination, navOptions))
    }

    override suspend fun navigateUp() {
        _navigatorActions.send(NavigatorAction.NavigateUp)
    }

    override suspend fun navigateWithResult(key: String, result: Any) {
        _navigatorActions.send(NavigatorAction.NavigateWithResult(key, result))
    }
}