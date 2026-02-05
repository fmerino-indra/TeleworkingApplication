package org.fmm.teleworking.ui.stats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.himanshoe.charty.bar.ComparisonBarChart
import com.himanshoe.charty.bar.data.BarGroup
import com.himanshoe.charty.color.ChartyColor
import com.himanshoe.charty.pie.PieChart
import com.himanshoe.charty.pie.config.LabelConfig
import com.himanshoe.charty.pie.config.PieChartConfig
import com.himanshoe.charty.pie.data.PieData
import kotlinx.datetime.LocalDate
import org.fmm.teleworking.domain.model.DayDto
import org.fmm.teleworking.domain.model.Modality
import org.fmm.teleworking.domain.model.stats.BasicStatsDto
import org.fmm.teleworking.domain.model.stats.MonthStatsDto
import org.fmm.teleworking.domain.model.stats.YearStatsDto
import org.fmm.teleworking.ui.calendar.toHexCode
import org.fmm.teleworking.ui.colors.COLOR_PRESENTIAL
import org.fmm.teleworking.ui.colors.COLOR_TELEWORK

@Composable
fun StatsScreen(viewModel: StatsViewModel) {
    StatsHeaderView(
        viewModel=viewModel,
//        onYearChange = {},
/*
        onLoadAnnualStats = {year ->
            viewModel.loadYearStats(year)
        },
        onLoadQuarterStats = { i: Int, i1: Int -> },

 */
        onBack = {}
    )
/*
    val statsUiState: StatsUIState by viewModel.statsUiState.collectAsState()

    when (statsUiState) {
        is StatsUIState.Loading -> {}
        is StatsUIState.Idle -> {
            StatsHeaderView(//basicStatsDto = (statsUiState as StatsUIState.Success).statsDto,
                onYearChange = {},
                onLoadAnnualStats = {year ->
                    viewModel.loadYearStats(year)
                },
                onLoadQuarterStats = { i: Int, i1: Int -> },
                onBack = {}
            )
        }
        is StatsUIState.Success -> {
            StatsContentView(
                basicStatsDto = (statsUiState as StatsUIState.Success).statsDto,
                onBack = {
                    viewModel.initData()
                }
            )
        }

    }
*/
}



/* -------------------- StatsView -------------------- */
@Composable
fun StatsHeaderView(
    viewModel: StatsViewModel,
//    onYearChange: (String) -> Unit,
//    onLoadAnnualStats: (Int) -> Unit,
//    onLoadQuarterStats: (Int, Int) -> Unit,
    onBack: () -> Unit
) {
    //val yearText = basicStatsDto.year.toString()
    //var yearText by remember { DayDto.now().year.toString() }
    var yearText by remember {
        mutableStateOf(DayDto.now().year.toString())
    }

    Column (modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Statistics", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Row () {
            OutlinedTextField(
                value = yearText,
                onValueChange = { newValue ->
                    yearText = newValue
                },
                label = { Text("Year") },
                readOnly = false,
                enabled = true,
                modifier = Modifier.width(140.dp)
            )
            Column (modifier = Modifier.fillMaxWidth()) {
// 4 RadioButtons
//                val radioOptions = listOf("Year", "Q1", "Q2", "Q3", "Q4")
                val radioOptions = listOf("Year", "Quarter")
                val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
                // Note that Modifier.selectableGroup() is essential to ensure correct accessibility behavior
                Column(modifier = Modifier.selectableGroup()) {
                    radioOptions.forEach { text ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                //.height(56.dp)
                                .selectable(
                                    selected = (text == selectedOption),
                                    onClick = { onOptionSelected(text) },
                                    role = Role.RadioButton
                                )
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (text == selectedOption),
                                onClick = null // null recommended for accessibility with screen readers
                            )
                            Text(
                                text = text,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                    }
                }
            }


        }
        Spacer(modifier = Modifier.height(12.dp))
        Row {
            Button(onClick = {
                viewModel.loadYearStats(yearText.toIntOrNull()?: DayDto.now().year)
                //onLoadAnnualStats(yearText.toIntOrNull()?: DayDto.now().year)
            }) {
                Text("Load Annual")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                //onLoadQuarterStats(yearText.toIntOrNull()?: DayDto.now().year, 1)
                viewModel.loadYearStats(yearText.toIntOrNull()?: DayDto.now().year)
            }) {
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


        StatsContentView(
            viewModel = viewModel,
            onBack = onBack
        )
    }
}


@Composable
fun StatsContentView(
    viewModel: StatsViewModel,
    onBack: () -> Unit
) {
    val statsUiState: StatsUIState by viewModel.statsUiState.collectAsState()


    when (statsUiState) {
        is StatsUIState.Loading -> {}
        is StatsUIState.Idle -> {
        }
        is StatsUIState.Success -> {
            val basicStatsDto: BasicStatsDto =
                (statsUiState as StatsUIState.Success).statsDto

            val yearText = basicStatsDto.year.toString()

            Column (modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text("Results: ${basicStatsDto.year}", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(12.dp))
                /*
                Row {
                    Button(onClick = { onBack() }) {
                        Text("Back")
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                 */
                Text("Year: ${basicStatsDto.year}")
                Text("Total days: ${basicStatsDto.totalDays}")
                Text("Working days: ${basicStatsDto.totalWorkingdays}")
                Text("Non working days: ${basicStatsDto.totalNonWorkingdays}")
                Text("Presential days: ${basicStatsDto.getPresentialCount()}")
                Text("Teleworking days: ${basicStatsDto.getTeleworkingCount()}")
                StatsPieChart(basicStatsDto)
            }
        }
    }
}


@Composable
private fun StatsPieChart(basicStatsDto: BasicStatsDto) {

    PieChart(
        data = {
            listOf(
                PieData("Presential", basicStatsDto.getPresentialCount().toFloat()),
                PieData("Teleworking", basicStatsDto.getTeleworkingCount().toFloat()),
            )
        },
        color = ChartyColor.Gradient(
            listOf(
                Color(COLOR_PRESENTIAL.toArgb()),
                Color(COLOR_TELEWORK.toArgb())
            )
        ),
        config = PieChartConfig(
            labelConfig = LabelConfig(
                shouldShowLabels = true,
                shouldShowLabelsOutside = false
            ),
//            showLabels = true,
//            labelPosition = LabelPosition.Outside,
        ),
    )
}

@Composable
private fun StatsComparisonBarChart(basicStatsDto: BasicStatsDto) {
    ComparisonBarChart(
        data = {
            listOf(
                BarGroup("Q1", listOf(45f, 52f)),
                BarGroup("Q2", listOf(58f, 63f)),
                BarGroup("Q3", listOf(72f, 68f)),
            )
        },
    )
}




@Preview(showBackground = true, widthDp =360, heightDp = 640)
@Composable
fun StatisticsPreview() {
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
        DayDto(LocalDate.parse("2026-01-11"), Modality.WEEKEND)
    )

    val dto = YearStatsDto(
        year = 2026,
        days = sampleDays
    )

    Surface {
        Column() {
/*
            StatsHeaderView(
                onYearChange = {},
                onLoadAnnualStats = {},
                onLoadQuarterStats = { i: Int, i1: Int -> },
                onBack = {}
            )
*/

/*
            Spacer(modifier = Modifier.height(12.dp))

            StatsContentView(
                basicStatsDto = dto,
                onBack = {}
            )

 */
        }
    }
}




private fun processState(statsUiState: StatsUIState): BasicStatsDto {
    return when (statsUiState) {
        is StatsUIState.Loading -> {
            val aux = MonthStatsDto.emptyMonthDto()
            aux
        }
        is StatsUIState.Idle -> MonthStatsDto.emptyMonthDto()
        is StatsUIState.Success -> statsUiState.statsDto
    }
}
