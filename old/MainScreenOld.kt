package org.fmm.teleworking.ui


import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate
import org.fmm.teleworking.domain.model.DayDto
import org.fmm.teleworking.domain.model.Modality

@Composable
fun MainScreenOld(viewModel: MainViewModel) {
    val days by viewModel.monthDays.collectAsState()

    MainScreenContentOld(
        days = days,
        onOpenYear = viewModel::openYear,
        onLoadMonth = viewModel::loadMonth,
        onSetModality = viewModel::setModality
    )
}

@Composable
fun DayRowOld(day: DayDto, onSetTelework: () -> Unit, onSetPresential: () -> Unit) {
    Card ( modifier = Modifier.fillMaxWidth().padding(vertical =  4.dp), elevation =
        CardDefaults.cardElevation(2.dp )) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(text = day.date.toString())
                Text(text =  "Modality: ${day.modality}")
            }
            if (!day.isWeekend()) {
                Row {
                    Button(onClick = onSetTelework) { Text("T") }
                    Spacer(modifier = Modifier.width(4.dp))
                    Button(onClick =  onSetPresential) { Text("P") }
                }
            } else {
                Text(if (day.isFestive()) "Festivo" else "No laborable")
            }
        }
    }
}

@Composable
fun MainScreenContentOld(
    days: List<DayDto>,
    onOpenYear: (Int) -> Unit,
    onLoadMonth: (Int, Int) -> Unit,
    onSetModality: (LocalDate, Modality) -> Unit
) {
    var yearText by remember { mutableStateOf("2026") }

    Column (modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = { onOpenYear(yearText.toInt()) }) {
                Text( "Open year")
            }
            OutlinedTextField(
                value = yearText,
                onValueChange = { yearText = it},
                label = { Text ("Year") },
                modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(8.dp))
//            Button(onClick = { viewModel.openYear(yearText.toInt()) }) {
//                Text( "Open Jan")
//            }
            Button(onClick = { onLoadMonth(yearText.toInt(), 1) }) {
                Text( "Load Jan")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn {
            items(days) { day ->
                DayRowOld(day = day,
                    onSetTelework = {
                        onSetModality(day.date, Modality.TELEWORK) },
                    onSetPresential = {
                        onSetModality( day.date, Modality.PRESENTIAL) })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenContentPreviewOld() {
    val sampleDays = listOf(
        DayDto(LocalDate.parse("2026-01-01"), Modality.FESTIVE),
        DayDto(LocalDate.parse("2026-01-02"), Modality.UNASSIGNED),
        DayDto(LocalDate.parse("2026-01-03"), Modality.WEEKEND),
        DayDto(LocalDate.parse("2026-01-04"), Modality.WEEKEND),
        DayDto(LocalDate.parse("2026-01-05"), Modality.UNASSIGNED),
        DayDto(LocalDate.parse("2026-01-06"), Modality.FESTIVE),
        DayDto(LocalDate.parse("2026-01-07"), Modality.UNASSIGNED),
        DayDto(LocalDate.parse("2026-01-08"), Modality.TELEWORK),
        DayDto(LocalDate.parse("2026-01-09"), Modality.PRESENTIAL),
        DayDto(LocalDate.parse("2026-01-10"), Modality.WEEKEND),
        DayDto(LocalDate.parse("2026-01-11"), Modality.WEEKEND),
    )
    MainScreenContentOld(
        days = sampleDays,
        onOpenYear = { _ ->},
        onLoadMonth = { _, _ -> },
        onSetModality = { _, _ -> }
    )
}