package org.fmm.teleworking.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number
import org.fmm.teleworking.domain.model.DayDto
import org.fmm.teleworking.domain.model.Modality
import org.fmm.teleworking.data.network.repository.CalendarRepository
import org.fmm.teleworking.domain.usecases.GetMonth
import org.fmm.teleworking.domain.usecases.OpenYear
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val openYearUC: OpenYear,
    private val getMonth: GetMonth
): ViewModel() {
    private val _monthDays = MutableStateFlow<List<DayDto>>(emptyList())
    val monthDays: StateFlow<List<DayDto>> = _monthDays

    fun openYear(year: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            openYearUC(year)
        }
    }

    fun loadMonth(year: Int,  month: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = getMonth(year, month)
            _monthDays.value = list
        }
    }
/*
    fun toggleFestive(year: Int, date: LocalDate, festive: Boolean) {
        viewModelScope.launch {
            repo.toggleFestive(year, date, festive)
            loadMonth(year, date.month.number ) // Recarga mes
        }
    }
*/
    fun setModality(date: LocalDate, modality: Modality) {
        /*
        viewModelScope.launch {
            repo.setModality(date, modality)
            val year = date.year
            val month = date.month.number
            loadMonth(year, month)
        }

         */
    }
}