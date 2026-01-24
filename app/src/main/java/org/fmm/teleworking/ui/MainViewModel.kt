package org.fmm.teleworking.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.fmm.teleworking.model.DayDto
import org.fmm.teleworking.model.Modality
import org.fmm.teleworking.repository.CalendarRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: CalendarRepository
): ViewModel() {
    private val _monthDays = MutableStateFlow<List<DayDto>>(emptyList())
    val monthDays: StateFlow<List<DayDto>> = _monthDays

    fun openYear(year: Int) {
        viewModelScope.launch { repo.openYear(year) }
    }

    fun loadMonth(year: Int,  month: Int) {
        viewModelScope.launch {
            val list = repo.getMonth(year, month)
            _monthDays.value = list
        }
    }

    fun toggleFestive(year: Int, date: String, festive: Boolean) {
        viewModelScope.launch {
            repo.toggleFestive(year, date, festive)
            loadMonth(year, date.substring(5,7).toInt()) // Recarga mes
        }
    }

    fun setModality(date: String, modality: Modality) {
        viewModelScope.launch {
            repo.setModality(date, modality)
            val year = date.substring(0,4).toInt()
            val month = date.substring(5,7).toInt()
            loadMonth(year, month)
        }
    }
}