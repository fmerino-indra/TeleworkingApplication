package org.fmm.teleworking.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.fmm.teleworking.domain.model.DayDto
import org.fmm.teleworking.domain.model.Modality
import org.fmm.teleworking.domain.model.stats.BasicStatsDto
import org.fmm.teleworking.domain.model.stats.MonthStatsDto
import org.fmm.teleworking.ui.calendar.CalendarViewModel
import org.fmm.teleworking.ui.calendar.MainScreenCalendarMonthEditor
import org.fmm.teleworking.ui.calendar.MainScreenMonth
import org.fmm.teleworking.ui.openyear.OpenYearScreen
import org.fmm.teleworking.ui.openyear.OpenYearView
import org.fmm.teleworking.ui.openyear.YearViewModel
import org.fmm.teleworking.ui.stats.StatsScreen
import org.fmm.teleworking.ui.stats.StatsViewModel

// Habría que definir aquí la clase sealed Screend

sealed class Screen {
    object None: Screen()
    object Month : Screen()
    object OpenYear: Screen()
    object Stats : Screen()
}


@Composable
//fun MainScreen(calendarViewModel: CalendarViewModel) {
fun MainScreenOld(
    onNavigateToStats: () -> Unit
) {
    // Quitar
    val calendarViewModel: CalendarViewModel = hiltViewModel()

    // obtain viewModel via Hilt if not provided (helps previews / tests)
//    val viewModel: CalendarViewModel = entryViewModel

    /* Dates */

    //val today = remember { LocalDate.now() }
    // current month/year defaults (start with current month)
    val today = remember {  Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date }
    val currentYear = today.year
    val currentMonth = today.monthNumber

    val isPreviousOpen: Boolean = remember {
        calendarViewModel.isYearOpened(currentYear-1)
    }
    val isNextOpen: Boolean = remember {
        calendarViewModel.isYearOpened(currentYear+1)
    }


    /*
    Navigation and menu
    */

    // screen selection
    var currentScreen by remember {
        mutableStateOf<Screen>(Screen.None)
    }

    // menu state for 3-dots menu
    var menuExpanded by remember {
        mutableStateOf(false)
    }


    // year input state (shared beween screens)
    var yearText by remember {
        mutableStateOf(today.year.toString())
    }

    // Collect days state from ViewModel
    //val days by viewModel.monthDays.collectAsState(initial = emptyList())
//    val monthDto by viewModel.monthDto.collectAsState()
    val uiState by calendarViewModel.uiState.collectAsState()

    val monthDto =
        when (uiState) {
            is UiState.Loading -> {
                val aux = MonthStatsDto.emptyMonthDto()
                aux
            }
            is UiState.Idle -> MonthStatsDto.emptyMonthDto()
            is UiState.Success -> (uiState as UiState.Success).monthDto
        }


    // when screen first composed: Load current month automatically
    LaunchedEffect(Unit) {
        // ensure yearText set to current year
        yearText = currentYear.toString()

        // open year (create calendar) optionally - comment if you don't want auto-open
        // viewModel.openYear(currentYear)

        //viewModel.loadMonth(currentYear, currentMonth)
    }

    // Call the UI-only composable, passing all callbacks and state
    MainScreenContentOld(
        /*
        calendarViewModel = calendarViewModel,
        monthDto = monthDto,
        yearText = yearText,
        currentMonth = currentMonth,
        currentYear = currentYear,

         */
        menuExpanded = menuExpanded,
        onMenuToggle = { menuExpanded = it },
        onNavigateToOpenYear = {
            menuExpanded = false
            currentScreen = Screen.OpenYear
        },
        onNavigateToStats = onNavigateToStats,
        /*
            {
            menuExpanded = false
            currentScreen = Screen.Stats
        },

         */
        onNavigateToMonth = {
            menuExpanded = false
            currentScreen = Screen.Month
            val y = yearText.toIntOrNull() ?: currentYear
            calendarViewModel.loadMonth(y, currentMonth)
        },
        /*
        onYearChange = { newYear -> yearText = newYear },
        onOpenYear = {y ->
            yearText = y.toString()
            calendarViewModel.openYear(y)
            // After opening, show month view (January)
            currentScreen = Screen.Month
            calendarViewModel.loadMonth(y, 1)
        },
        onLoadMonth = { y, m ->
            calendarViewModel.loadMonth(y,m)
            currentScreen = Screen.Month
        },
        onSetTelework = { date -> calendarViewModel.setModality(date, Modality.TELEWORK)},
        onSetPresential = { date -> calendarViewModel.setModality(date, Modality.PRESENTIAL)},
        onSetModality = { date, modality ->
            calendarViewModel.setModality(date = date, modality = modality) },

         */

        currentScreen = currentScreen
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenContentOld(
    menuExpanded: Boolean,
    onMenuToggle: (Boolean) -> Unit,
    onNavigateToOpenYear: () -> Unit,
    onNavigateToStats: () -> Unit,
    onNavigateToMonth: () -> Unit,
    currentScreen: Screen
) {

    Scaffold   (

        topBar = {
            MainAppBar(onNavigateToStats = onNavigateToStats)
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()) {
            when (currentScreen) {
                is Screen.None -> {

                }
                is Screen.Month -> {
                    MainScreenMonth()
                    /*
                    MainScreenCalendarMonthEditor(
                        year = yearText.toIntOrNull() ?: currentYear,
                        month = currentMonth,
                        monthDto = monthDto as MonthStatsDto,
                        onApplyModality = { date, modality ->
                            onSetModality(date, modality)
                        })

                     */
                }
                is Screen.OpenYear -> {
                    OpenYearScreen()
                }
                is Screen.Stats -> {
                    StatsScreen()
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainAppBar(onNavigateToStats: () -> Unit) {
    TopAppBar(
        title = { Text("Control Teletrabajo") },
        actions =  {
            // three dots menu
            AppBarAction(
                imageVector = Icons.Default.MoreVert,
                onNavigateToMonth = {},
                onNavigateToOpenYear = {},
                onNavigateToStats = onNavigateToStats,
            )
            /*
                        IconButton (onClick = { }) {
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Menu")
                        }
                        DropdownMenu (
                            expanded = menuExpanded,
            //                        onDismissRequest = { onMenuToggle(false)  }
                            onDismissRequest = {  }
                        ) {
                            DropdownMenuItem(
                                text = {Text("Open Year")},
                                onClick = {
            //                                onMenuToggle(false)
            //                                onNavigateToOpenYear()
                                }
                            )
                            DropdownMenuItem(
                                text = {Text("Statistics")},
                                onClick = {
            //                                onMenuToggle(false)
            //                                onNavigateToStats()
                                }
                            )
                            DropdownMenuItem(
                                text = {Text("Load Month")},
                                onClick = {
            //                                onMenuToggle(false)
            //                                onNavigateToMonth()
                                }
                            )
                        } // DropdownMenu

             */
        }
    ) // TopAppBar

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SecondaryAppBar (title:String,
                             backStack:()->Unit) {
    // menu state for 3-dots menu

    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = { backStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
    ) // TopAppBar
}

@Composable
private fun AppBarAction(
    imageVector: ImageVector,
    onNavigateToMonth: () -> Unit,
    onNavigateToOpenYear: () -> Unit,
    onNavigateToStats: () -> Unit,
) {
    var menuExpanded by remember {
        mutableStateOf(false)
    }

    val onMenuToggle: (Boolean) -> Unit = {newValue ->
        menuExpanded = newValue
    }

    IconButton(onClick = { menuExpanded = !menuExpanded }) {
        Icon(
            imageVector = imageVector,
            contentDescription = "Menu"
        )
    }
    DropdownMenu (
        expanded = menuExpanded,
//        onDismissRequest = { onMenuToggle(false)  }
        onDismissRequest = {
            Log.d("[FMMP]","Solicitud de onDismissRequest")
            menuExpanded = false
        }
    ) {
        DropdownMenuItem(
            text = {Text("Open Year")},
            onClick = {
                onMenuToggle(false)
            }
        )
        DropdownMenuItem(
            text = {Text("Statistics")},
            onClick = {
                onMenuToggle(false)
                onNavigateToStats()
            }
        )
        DropdownMenuItem(
            text = {Text("Load Month")},
            onClick = {
                onMenuToggle(false)
            }
        )
    } // DropdownMenu
}


@Preview()
@Composable
fun PreviewOld() {
}

@Preview(showBackground = true, widthDp =360, heightDp = 640)
@Composable
fun CalendarMonthEditorPreviewOld() {
    val sampleDays = listOf(
        DayDto(LocalDate.parse("2026-01-01"), Modality.FESTIVE),
        DayDto(LocalDate.parse("2026-01-02"), Modality.TELEWORK),
        DayDto(LocalDate.parse("2026-01-03"), Modality.WEEKEND),
        DayDto(LocalDate.parse("2026-01-04"), Modality.WEEKEND),
        DayDto(LocalDate.parse("2026-01-05"), Modality.HOLIDAY),
        DayDto(LocalDate.parse("2026-01-06"), Modality.FESTIVE),
        DayDto(LocalDate.parse("2026-01-07"), Modality.PRESENTIAL),
        DayDto(LocalDate.parse("2026-01-08"), Modality.TELEWORK),
        DayDto(LocalDate.parse("2026-01-09"), Modality.PRESENTIAL),
        DayDto(LocalDate.parse("2026-01-10"), Modality.WEEKEND),
        DayDto(LocalDate.parse("2026-01-11"), Modality.WEEKEND),
    )
    Surface {
        MainScreenCalendarMonthEditor(
            year = 2026,
            month = 1,
            monthDto = MonthStatsDto(year = 2026, month = 1, sampleDays),
            onApplyModality = { dateIso, modality -> },
            isPreviousNavigable = false,
            isNextNavigable = true,
            onPrevious = { },
            onNext = {}
        )}
}

