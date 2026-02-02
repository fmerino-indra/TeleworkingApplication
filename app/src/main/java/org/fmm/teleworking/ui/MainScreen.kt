package org.fmm.teleworking.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import org.fmm.teleworking.domain.model.DayDto
import org.fmm.teleworking.domain.model.Modality

sealed class Screen {
    object Month : Screen()
    object OpenYear: Screen()
    object Stats : Screen()
}
@Composable
fun MainScreen (entryViewModel: MainViewModel) {
    // obtain viewModel via Hilt if not provided (helps previews / tests)
    val viewModel: MainViewModel = entryViewModel


    // screen selection
    var currentScreen by remember {
        mutableStateOf<Screen>(Screen.Month)
    }

    // menu state for 3-dots menu
    var menuExpanded by remember {
        mutableStateOf(false)
    }

    // current month/year defaults (start with current month)
    val today = remember {  Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date }

    // year input state (shared beween screens)
    var yearText by remember {
        mutableStateOf(today.year.toString())
    }

    //val today = remember { LocalDate.now() }
    val currentYear = today.year
    val currentMonth = today.monthNumber
    
    // Collect days state from ViewModel
    val days by viewModel.monthDays.collectAsState()
    
    // when screen first composed: Load current month automatically
    LaunchedEffect(Unit) {
        // ensure yearText set to current year
        yearText = currentYear.toString()
        
        // open year (create calendar) optionally - comment if you don't want auto-open
        // viewModel.openYear(currentYear)
        viewModel.loadMonth(currentYear, currentMonth)
    }

    // Call the UI-only composable, passing all callbacks and state
    MainScreenContent(
        days = days,
        yearText = yearText,
        currentMonth = currentMonth,
        currentYear = currentYear,
        menuExpanded = menuExpanded,
        onMenuToggle = { menuExpanded = it },
        onNavigateToOpenYear = {
            menuExpanded = false
            currentScreen = Screen.OpenYear
        },
        onNavigateToStats = {
            menuExpanded = false
            currentScreen = Screen.Stats
        },
        onNavigateToMonth = {
            menuExpanded = false
            currentScreen = Screen.Month
            val y = yearText.toIntOrNull() ?: currentYear
            viewModel.loadMonth(y, currentMonth)
        },
        onYearChange = { newYear -> yearText = newYear },
        onOpenYear = {y ->
            yearText = y.toString()
            viewModel.openYear(y)
            // After opening, show month view (January)
            currentScreen = Screen.Month
            viewModel.loadMonth(y, 1)
        },
        onLoadMonth = { y, m ->
            viewModel.loadMonth(y,m)
            currentScreen = Screen.Month
        },
        onSetTelework = { date -> viewModel.setModality(date, Modality.TELEWORK)},
        onSetPresential = { date -> viewModel.setModality(date, Modality.PRESENTIAL)},
        currentScreen = currentScreen
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenContent(
    days: List<DayDto>,
    yearText: String,
    currentMonth: Int,
    currentYear: Int,
    menuExpanded: Boolean,
    onMenuToggle: (Boolean) -> Unit,
    onNavigateToOpenYear: () -> Unit,
    onNavigateToStats: () -> Unit,
    onNavigateToMonth: () -> Unit,
    onYearChange: (String) -> Unit,
    onOpenYear: (Int) -> Unit,
    onLoadMonth: (Int, Int) -> Unit,
    onSetTelework: (LocalDate) -> Unit,
    onSetPresential: (LocalDate) -> Unit,
//    onSetModality: (LocalDate, Modality) -> Unit,
    currentScreen: Screen
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Control Teletrabajo") },
                actions =  {
                    // three dots menu
                    IconButton (onClick = { onMenuToggle(true) }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Menu")
                    }
                    DropdownMenu (
                        expanded = menuExpanded,
                        onDismissRequest = { onMenuToggle(false)  }
                    ) {
                        DropdownMenuItem(
                            text = {Text("Open Year")},
                            onClick = {
                                onMenuToggle(false)
                                onNavigateToOpenYear()
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
                                onNavigateToMonth()
                            }
                        )
                    } // DropdownMenu
                }
            ) // TopAppBar
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()) {
            when (currentScreen) {
                is Screen.Month -> {

                    MonthView(
                        yearText = yearText,
                        onYearChange = onYearChange,
                        onOpenYear = { y -> onOpenYear(y) },
                        days = days,
                        onSetTelework = { date -> onSetTelework(date) },
                        onSetPresential = { date -> onSetPresential(date) },
                        onLoadMonth = { y,m -> onLoadMonth(y,m)},
                        currentMonth = currentMonth
                    )
                }
                is Screen.OpenYear -> {
                    OpenYearView(
                        yearText = yearText,
                        onYearChange = onYearChange,
                        onOpenYear = { y -> onOpenYear(y) },
                        onCancel =  { /* Navigate back to the month */
                            onNavigateToMonth()
                        }
                    )
                }
                Screen.Stats -> {
                    StatsView(
                        yearText = yearText,
                        onYearChange = onYearChange,
                        onLoadAnnualStats = { y ->
                            /* delegate to VM in MainScreen */
                            onLoadMonth(y,1)
                        },
                        onLoadQuarterStats = { y,q ->
                            /* same: delegate*/
                            onLoadMonth(y,(q-1) *3+1)
                        },
                        onBack = { onNavigateToMonth() }
                    )
                }
            }
        }
    }
}

/* --------------- MonthView (UI) ---------------- */
@Composable
fun MonthView(
    yearText: String,
    onYearChange: (String) -> Unit,
    onOpenYear: (Int) -> Unit,
    days: List<DayDto>,
    onSetTelework: (LocalDate) -> Unit,
    onSetPresential: (LocalDate) -> Unit,
    onLoadMonth: (Int, Int) -> Unit,
    currentMonth: Int
    ) {
    Column (modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row {
            OutlinedTextField(
                value =  yearText,
                onValueChange = onYearChange,
                label =  { Text("Year")},
                modifier = Modifier.width(140.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                val y = yearText.toIntOrNull() ?: now().year
                onOpenYear(y)
            }) {
                Text("Open Year")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                val y = yearText.toIntOrNull() ?: now().year
                onLoadMonth(y,currentMonth)
            }) {
                Text("Load CMonth")
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        if (days.isEmpty()) {
            Text("No data for this month. Try 'Open Year' or 'Load Current Month'.",
                modifier = Modifier.padding(16.dp))
            return@Column
        }

        LazyColumn (modifier= Modifier.fillMaxSize()) {
            items(days, key={it.date.toString()}) {day ->
                DayRow(
                    day=day,
                    onSetTelework = { onSetTelework(day.date)},
                    onSetPresential = { onSetPresential(day.date)}
                )
            }
        }
    }
}

/* ------------------- OpenYearView --------------------*/

@Composable
fun OpenYearView(
    yearText: String,
    onYearChange: (String) -> Unit,
    onOpenYear: (Int) -> Unit,
    onCancel: () -> Unit
)  {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Open Year", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value =yearText, onValueChange = onYearChange, label = { Text("Year") })
        Spacer(modifier = Modifier.height(12.dp))
        Row {
            Button (onClick = { onOpenYear( yearText.toIntOrNull() ?: now().year) }) {
                Text("Open")
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton( onClick = onCancel) { Text("Cancel") }
        }
    }
}

/* -------------------- StatsView -------------------- */
@Composable
fun StatsView(
    yearText: String,
    onYearChange: (String) -> Unit,
    onLoadAnnualStats: (Int) -> Unit,
    onLoadQuarterStats: (Int, Int) -> Unit,
    onBack: () -> Unit
) {
    Column (modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Statistics", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = yearText,
            onValueChange = onYearChange,
            label = { Text("Year") },
            modifier = Modifier.width(140.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row {
            Button(onClick = { onLoadAnnualStats(yearText.toIntOrNull()?: now().year) }) {
                Text("Load Annual")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { onLoadQuarterStats(yearText.toIntOrNull()?: now().year, 1) }) {
                Text("Load Q1")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { onBack() }) {
                Text("Back")
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text("Note: Results are provided by the ViewModel (use Logcat or add state exposure to " +
                "render results).")
    }
}

/* ------------------- DayRow -------------- */

@Composable
fun DayRow(day: DayDto, onSetTelework: () -> Unit, onSetPresential: () -> Unit) {
    Card ( modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), elevation = CardDefaults.cardElevation(2.dp )) {
        Row (modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement =
            Arrangement.SpaceBetween) {
            Column {
                val display = day.date.format(localDateFormat())
                Text(display)
                Text(text =  "Modality: ${day.modality}")
            }
            if (!day.festive && !day.weekend) {
                Row {
                    Button(onClick = onSetTelework) { Text("T")}
                    Spacer(modifier = Modifier.width(4.dp))
                    Button(onClick = onSetPresential) { Text("P")}
                }
            } else {
                Text(if (day.festive) "Festive" else "Not working day"

                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp =360, heightDp = 640)
@Composable
fun MainScreenContentPreview() {
    val sampleDays = listOf(
        DayDto(LocalDate.parse("2026-01-01"), Modality.FESTIVE, false, true),
        DayDto(LocalDate.parse("2026-01-02"), Modality.UNASSIGNED, false, false),
        DayDto(LocalDate.parse("2026-01-03"), Modality.WEEKEND, true, false),
        DayDto(LocalDate.parse("2026-01-04"), Modality.WEEKEND, true, false),
        DayDto(LocalDate.parse("2026-01-05"), Modality.UNASSIGNED, false, false),
        DayDto(LocalDate.parse("2026-01-06"), Modality.FESTIVE, false, true),
        DayDto(LocalDate.parse("2026-01-07"), Modality.UNASSIGNED, false, false),
        DayDto(LocalDate.parse("2026-01-08"), Modality.TELEWORK, false, false),
        DayDto(LocalDate.parse("2026-01-09"), Modality.PRESENTIAL, false, false),
        DayDto(LocalDate.parse("2026-01-10"), Modality.WEEKEND, true, false),
        DayDto(LocalDate.parse("2026-01-11"), Modality.WEEKEND, true, false),
    )
    MainScreenContent(
        days = sampleDays,
        yearText = "2025",
        currentMonth = 1,
        currentYear = 2025,
        menuExpanded = false,
        onMenuToggle = {},
        onNavigateToOpenYear = {},
        onNavigateToStats = {},
        onNavigateToMonth = {},
        onYearChange = {},
        onOpenYear = {},
        onLoadMonth = {_,_->},
        onSetTelework = {},
        onSetPresential = {},
        currentScreen = Screen.Month
    )
}

private fun now(): LocalDate =
    Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

private fun localDateFormat(): DateTimeFormat<LocalDate> =
    LocalDate.Format {
        dayOfWeek(DayOfWeekNames.ENGLISH_ABBREVIATED) // Para el "EEE"
        char(',')
        char(' ')
        dayOfMonth()
        char(' ')
        monthNumber()
        char(' ')
        year()
    }
