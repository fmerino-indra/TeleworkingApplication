package org.fmm.teleworking.ui.openyear

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.fmm.teleworking.domain.model.DayDto
import org.fmm.teleworking.domain.model.stats.BasicStatsDto

@Composable
fun OpenYearScreen() {

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
            Button (onClick = { onOpenYear( yearText.toIntOrNull() ?: DayDto.now().year) }) {
                Text("Open")
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton ( onClick = onCancel) { Text("Cancel") }
        }
    }
}

