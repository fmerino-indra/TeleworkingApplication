package org.fmm.teleworking.ui.stats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.text.font.FontWeight
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
import org.fmm.teleworking.domain.model.stats.YearStatsDto
import org.fmm.teleworking.ui.colors.COLOR_PRESENTIAL
import org.fmm.teleworking.ui.colors.COLOR_TELEWORK
import org.fmm.teleworking.ui.colors.COLOR_TRAVEL

@Composable
fun StatsScreen(viewModel: StatsViewModel) {
    Column (modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        StatsHeaderView(
            onLoadAnnualStats = { year: Int -> viewModel.loadYearStats(year) },
            onLoadQuarterStats = { year: Int, quarter: Int ->
                viewModel.loadQuarterStats(
                    year,
                    quarter
                )
            },
            onLoadAllQuarterStats = { year: Int ->
                viewModel.loadAllQuartersStats(year)
            },
            onBack = {}
        )
        StatsContent(
            viewModel = viewModel,
//            onBack = onBack
        )

    }
}



/* -------------------- StatsView -------------------- */
@Composable
fun StatsHeaderView(
//    viewModel: StatsViewModel,
    onLoadAnnualStats: (Int) -> Unit,
    onLoadQuarterStats: (Int, Int) -> Unit,
    onLoadAllQuarterStats: (Int) -> Unit,
    onBack: () -> Unit
) {
    var yearText by remember {
        mutableStateOf(DayDto.now().year.toString())
    }

//    Column (modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Text("Statistics", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        val radioOptions = listOf("Year", "Quarter 1", "All Quarters")
        val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
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
        Spacer(modifier = Modifier.height(6.dp))
        Row {
            Button(onClick = {
                if (selectedOption == "Year")
                    onLoadAnnualStats(yearText.toIntOrNull()?: DayDto.now().year)
                else if (selectedOption == "Quarter 1")
                    onLoadQuarterStats(yearText.toIntOrNull()?: DayDto.now().year, 1)
                else
                    onLoadAllQuarterStats(yearText.toIntOrNull()?: DayDto.now().year)
            }) {
                Text("Load")
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
        //Spacer(modifier = Modifier.height(12.dp))
        /*
        StatsContent(
            viewModel = viewModel,
            onBack = onBack
        )

         */
//    }

}


@Composable
fun StatsContent(
    viewModel: StatsViewModel,
//    onBack: () -> Unit
) {
    val listStatsUiState: StatsUIState by viewModel.statsUiState.collectAsState()


    when (listStatsUiState) {
        is StatsUIState.Loading -> {}
        is StatsUIState.Idle -> {
        }
        is StatsUIState.Success -> {
            val basicStatsDto: List<BasicStatsDto> =
                (listStatsUiState as StatsUIState.Success).listStatsDto
            Column (Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())) {
                StatsContentView(basicStatsDto)
            }


        }
    }
}

@Composable
fun StatsContentView(listBasicStatsDto: List<BasicStatsDto>){
//val basicStatsDto = listBasicStatsDto[0]
    listBasicStatsDto.forEach { basicStatsDto ->
        Card (Modifier
            .fillMaxWidth()
            .padding(6.dp)
        //    .fillMaxHeight()
        ){
            Row (Modifier.padding(6.dp)) {
                Column(
                    modifier = Modifier
                        //.fillMaxWidth()
                        //.fillMaxHeight()
                        .padding(1.dp)
                    //.verticalScroll(rememberScrollState())
                ) {
                    Text(
                        "Results: ${basicStatsDto.year}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text("Total days: ${basicStatsDto.totalDays}")
                    Text("Working days: ${basicStatsDto.totalWorkingdays}")
                    Text("Non working days: ${basicStatsDto.totalNonWorkingdays}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Presential days: ${basicStatsDto.getPresentialCount()}")
                    Text("Teleworking days: ${basicStatsDto.getTeleworkingCount()}")
                    Text("Travel days: ${basicStatsDto.getTravelCount()}")
                    /*
                Spacer(modifier = Modifier.height(8.dp))
                Text("From month: ${basicStatsDto.fromMonth}")
                Text("Until month: ${basicStatsDto.untilMonth}")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Weekend days: ${basicStatsDto.getWeekendCount()}")
                Text("Festive days: ${basicStatsDto.getFestiveCount()}")
                Text("Holidays days: ${basicStatsDto.getHolidayCount()}")
*/

                } // Column
                /*
                if (basicStatsDto.getTravelCount() > 0 &&
                    basicStatsDto.getPresentialCount() > 0 &&
                    basicStatsDto.getTeleworkingCount() > 0 )

                 */
                StatsPieChartOld(basicStatsDto)


            } // Row
        } // Card
    } // forEach
}

@Composable
private fun StatsPieChart(basicStatsDto: BasicStatsDto) {
    Column(
        modifier = Modifier.padding(1.dp)
    ) {
        PieChart(
            modifier = Modifier.fillMaxSize(),
            data = {
                listOf(
                    PieData(
                        "Presential", basicStatsDto.getPresentialCount().toFloat(),
                        color = Color(COLOR_PRESENTIAL.toArgb())
                    ),
                    PieData(
                        "Teleworking", basicStatsDto.getPresentialCount().toFloat(),
                        color = Color(COLOR_TELEWORK.toArgb())
                    )
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
                    shouldShowLabels = false,
                    shouldShowLabelsOutside = false
                ),
            ),
        )
    }
}

@Composable
private fun StatsPieChartOld(basicStatsDto: BasicStatsDto) {
    if ((basicStatsDto.getPresentialCount() == 0) &&
        (basicStatsDto.getTeleworkingCount() == 0) &&
        (basicStatsDto.getTravelCount() == 0)
    ) {
        return
    }

    val theData: MutableList<PieData> = mutableListOf()
    val colors: MutableList<Color> = mutableListOf()

    if (basicStatsDto.getPresentialCount() > 0) {
        theData.add(
            PieData("Presential", basicStatsDto.getPresentialCount().toFloat(),
                color = Color(COLOR_PRESENTIAL.toArgb()))
        )
        colors.add(Color(COLOR_PRESENTIAL.toArgb()))
    }
    if (basicStatsDto.getTeleworkingCount() > 0) {
        theData.add(
            PieData("Teleworking", basicStatsDto.getTeleworkingCount().toFloat(),
                color=Color(COLOR_TELEWORK.toArgb()))
        )
        colors.add(Color(COLOR_TELEWORK.toArgb()))
    }
    if (basicStatsDto.getTravelCount() > 0) {
        theData.add(
            PieData("Travelling", basicStatsDto.getTravelCount().toFloat(),
                color =  Color(COLOR_TRAVEL.toArgb()))
        )
        colors.add(Color(COLOR_TRAVEL.toArgb()))
    }
//    Column (Modifier.fillMaxWidth().fillMaxHeight()){
    PieChart(
        modifier = Modifier.fillMaxSize().padding(5.dp),
        data = { theData.toList() },

        color = ChartyColor.Gradient(colors
            /*
            listOf(
                Color(COLOR_PRESENTIAL.toArgb()),
                Color(COLOR_TELEWORK.toArgb()),
                Color(COLOR_TRAVEL.toArgb())
            )

             */
        ),
        config = PieChartConfig(
            labelConfig = LabelConfig(
                shouldShowLabels = false,
                shouldShowLabelsOutside = false
            ), donutHoleRatio = 0.5f
        ),
    )
//    }
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
        DayDto(LocalDate.parse("2026-01-11"), Modality.WEEKEND),
        DayDto(LocalDate.parse("2026-01-12"), Modality.TRAVEL),
        DayDto(LocalDate.parse("2026-01-13"), Modality.TRAVEL)
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
            Spacer(modifier = Modifier.height(12.dp))
*/
            StatsContentView(listBasicStatsDto = listOf(dto))
        }
    }
}
