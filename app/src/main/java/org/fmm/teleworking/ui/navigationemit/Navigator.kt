package org.fmm.teleworking.ui.navigation

import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.ComposeNavigator.Destination
import kotlinx.coroutines.flow.Flow

interface Navigator {
    val startDestination: Destination
    val navigatorActions: Flow<NavigatorAction>

    suspend fun navigate(
        destination: Destination,
        navOptions: NavOptionsBuilder.()->Unit
    )
    suspend fun navigateUp()
    suspend fun navigateWithResult(key: String,  result:Any)
}