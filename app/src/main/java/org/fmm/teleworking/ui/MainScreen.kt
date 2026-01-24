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
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.fmm.teleworking.model.DayDto
import org.fmm.teleworking.model.Modality

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val days by viewModel.monthDays.collectAsState()
    var yearText by remember { mutableStateOf("2025") }
    val scope = rememberCoroutineScope ()

    Column (modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row {
            OutlinedTextField(value = yearText, onValueChange = { yearText = it}, label = { Text ("Year") } )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { viewModel.openYear(yearText.toInt()) }) {
                Text( "Load Jan")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn {
            items(days) { day ->
                DayRow(day = day,
                    onSetTelework = { viewModel.setModality(day.date, Modality.TELEWORK) },
                    onSetPresential = { viewModel.setModality( day.date, Modality.PRESENTIAL) })
            }
        }
    }
}

@Composable
fun DayRow(day: DayDto, onSetTelework: () -> Unit, onSetPresential: () -> Unit) {
    Card ( modifier = Modifier.fillMaxWidth().padding(vertical =  4.dp), elevation = 2.dp ){
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(text = day.date)
                Text(text =  "Modality: ${day.modality}")
            }
            if (!day.festive && !day.weekend) {
                Row {
                    Button(onClick = onSetTelework) { Text("T") }
                    Spacer(modifier = Modifier.width(4.dp))
                    Button(onClick =  onSetPresential) { Text("P") }
                }
            } else {
                Text(if (day.festive) "Festivo" else "No laborable")
            }
        }
    }
}