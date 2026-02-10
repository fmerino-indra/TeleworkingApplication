package org.fmm.teleworking.ui.openyear

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.fmm.teleworking.data.model.YearConfig
import org.fmm.teleworking.domain.model.DayDto

@Composable
fun OpenYearScreen() {
    val viewModel: YearViewModel = hiltViewModel<YearViewModel>()

    LaunchedEffect(Unit) {
        viewModel.getOpenedYears()
    }

    val yearUiState by viewModel.yearUiState.collectAsState()

    var yearList by remember {
        when (yearUiState) {
            YearUiState.Idle -> { mutableStateOf(emptyList<YearConfig>())}
            YearUiState.Loading -> { mutableStateOf(emptyList<YearConfig>())}
            is YearUiState.Success -> {
                mutableStateOf((yearUiState as YearUiState.Success).yearConfigList)
            }
        }
    }

    val today = remember {
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    }
    var currentYear by remember { mutableIntStateOf(today.year) }

    var isCurrentYearOpened: Boolean by remember {
        mutableStateOf (viewModel.isYearOpened(currentYear))
    }

    val modifier = Modifier
    Column (modifier) {
        ListYearContent(yearList, modifier = modifier)

        OpenYearView(
            yearText = currentYear.toString(),
            isCurrentYearOpened = isCurrentYearOpened,
            onYearChange = {
                currentYear = it.toIntSave()
                isCurrentYearOpened = viewModel.isYearOpened(currentYear)
            },
            onOpenYear = { viewModel.openYear(currentYear) },
            onCancel = { },
            modifier = modifier
        )

    }
}

@Composable
fun ListYearContent(yearConfigList: List<YearConfig>, modifier: Modifier=Modifier) {
    Column (modifier) {
        Text("Opened years", style = MaterialTheme.typography.headlineSmall)
        /*
    Column (modifier = modifier.padding(vertical = 4.dp)) {
        yearConfigList.forEach {year ->
            YearDetail(year, modifier)
        }
    }

     */
        LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
            items(yearConfigList) { year ->
                YearDetail(year, modifier)
            }
        }
    }
}
@Composable
fun ListYearScreenLazy(yearConfigList: List<YearConfig>, modifier: Modifier=Modifier) {
    LazyColumn (modifier = modifier.padding(vertical = 4.dp)) {
        items(items = yearConfigList) {year ->
            YearDetail(year, modifier)
        }
    }
}

/* ------------------- OpenYearView --------------------*/

@Composable
fun YearDetail(yearConfig: YearConfig, modifier: Modifier) {
    Card(modifier = modifier.padding(4.dp),
        ) {
        Text(yearConfig.year.toString(),
            modifier = modifier.fillMaxWidth().padding(8.dp),
            textAlign = TextAlign.Center)
    }
}

@Composable
fun OpenYearView(
    yearText: String,
    isCurrentYearOpened: Boolean,
    onYearChange: (String) -> Unit,
    onOpenYear: (Int) -> Unit,
    onCancel: () -> Unit,
    modifier:  Modifier
)  {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)) {
        Text("Open Year", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value =yearText, onValueChange = onYearChange, label = { Text("Year") })
        Spacer(modifier = Modifier.height(12.dp))
        Row {
            Button (onClick = {
                onOpenYear( yearText.toIntOrNull() ?: DayDto.now().year) },
                enabled = !isCurrentYearOpened
            ) {
                Text("Open")
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton ( onClick = onCancel) { Text("Cancel") }
        }
    }
}
@Composable
private fun ListYears() {

}

@Preview(showBackground = true, widthDp =360, heightDp = 640)
@Composable
fun Preview() {
    OpenYearView(
        yearText = "2026",
        isCurrentYearOpened = true,
        onYearChange = {  },
        onOpenYear = {  },
        onCancel = {  },
        modifier =  Modifier
    )
}
@Preview(showBackground = true, widthDp =360, heightDp = 640)
@Composable
fun PreviewList() {
    val sampleList = listOf<YearConfig>(
        YearConfig(2021),
        YearConfig(2023),
        YearConfig(2024),
        YearConfig(2025),
        YearConfig(2027),
        YearConfig(2028),
        YearConfig(2029),
    )
    ListYearContent(sampleList, Modifier)
}
/*
fun String.intOrString(): Any {
    val v = toIntOrNull()
    return when(v) {
        null -> this
        else -> v
    }
}

 */

fun String.toIntSave() = toIntOrNull() ?: 0
/*
"4".intOrString() // 4
"x".intOrString() // x
*/
