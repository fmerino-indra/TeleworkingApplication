package org.fmm.fmm_navigation.ui.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class NavItem (
    internal val baseRoute: String,
    val title: String,
    private val navArgs: List<NavArgs> = emptyList()
    ) {
        object MainNavItem : NavItem("main", title = "Teleworking control")

        object CalendarNavItem : NavItem("month", title = "Monthly control")

        object YearMonthNavItem : NavItem("month2",
            title = "Monthly control",
            listOf(
                NavArgs.ActualYear,
                NavArgs.ActualMonth
            )
        ) {
            fun createRoute(actualYear:Int, actualMonth: Int) =
                "$baseRoute/$actualYear/$actualMonth"

        }
        object StatisticsNavItem : NavItem("statistics", title = "Statistics")

        object ListYearsNavItem: NavItem("open", title = "Year Management")
    /*
        object DetailNavItem : NavItem("detail", listOf(NavArgs.MediaId)) {
            fun createRoute(mediaId: Int) = "$baseRoute/$mediaId"
        }

     */

        val route = run {
            val argValues = navArgs.map { "{${it.key}}" }
            listOf(baseRoute)
                .plus(argValues)
                .joinToString("/")
        }

        val args = navArgs.map {
            navArgument(it.key) { type = it.navType }
        }

    }

    enum class NavArgs(val key: String, val navType: NavType<*>) {
        MediaId("mediaId", NavType.IntType),
        ActualYear(key="actualYear", NavType.IntType),
        ActualMonth(key="actualMonth", NavType.IntType)
    }