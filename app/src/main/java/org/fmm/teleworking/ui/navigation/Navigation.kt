package org.fmm.fmm_navigation.ui.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.fmm.teleworking.ui.calendar.MainScreenMonth
import org.fmm.teleworking.ui.openyear.OpenYearScreen
import org.fmm.teleworking.ui.stats.StatsScreen

@ExperimentalFoundationApi
@Composable
fun Navigation(navController: NavHostController, paddingValues: PaddingValues) {

    NavHost(
        navController = navController,
        startDestination = NavItem.MainNavItem.route,
        modifier =  Modifier.padding(paddingValues)
    ) {
        // Esta opción es la de
        composable(NavItem.MainNavItem) {
/*
            MainScreen(
                onNavigate = { navController.navigate(NavItem.DetailNavItem.createRoute(it)) }
            )

 */
/*
            MainScreen( {
                navController.navigate(NavItem.StatisticsNavItem.route)
            })

 */
        }
        composable(NavItem.StatisticsNavItem) { backStackEntry ->
            StatsScreen()
        }

        composable(NavItem.YearMonthNavItem) { backStackEntry ->
            MainScreenMonth()
        }
        composable(NavItem.CalendarNavItem) { backStackEntry ->
            MainScreenMonth()
        }

        composable(NavItem.ListYearsNavItem) { backStackEntry ->
            OpenYearScreen()
            /*
            DetailScreen(
                mediaId = backStackEntry.findArg(NavArgs.MediaId.key),
                onUpClick = { navController.popBackStack() }
            )

             */
        }
    }
}
/*
fun NavGraphBuilder.graphA() {
    navigation< ComposeNavigator.Destination.Menu>(
        startDestination = ComposeNavigator.Destination.ScreenA
    ) {

    }
}

 */

private fun NavGraphBuilder.composable(
    navItem: NavItem,
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(
        route = navItem.route,
        arguments = navItem.args
    ) {
        content(it)
    }
}

private inline fun <reified T> NavBackStackEntry.findArg(key: String): T {
    val value = arguments?.get(key)
    requireNotNull(value)
    return value as T
}