package org.fmm.teleworking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.fmm.teleworking.ui.MainScreen
import org.fmm.teleworking.ui.calendar.CalendarViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val vm = hiltViewModel<CalendarViewModel>()
            val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            val currentYear = today.year
            val currentMonth = today.monthNumber

            vm.initData(currentYear, currentMonth)
//            MainScreenOld(vm)
//            MainScreen(vm)
            MainScreen(vm)

        }
    }
}