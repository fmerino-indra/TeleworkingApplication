package org.fmm.teleworking.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number
import org.fmm.teleworking.domain.model.DayDto
import org.fmm.teleworking.domain.model.Modality
import org.fmm.teleworking.domain.model.MonthStatsDto
import org.fmm.teleworking.domain.usecases.GetMonth
import org.fmm.teleworking.domain.usecases.IsYearOpened
import org.fmm.teleworking.domain.usecases.OpenYear
import org.fmm.teleworking.domain.usecases.SetModality
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val openYearUC: OpenYear,
    private val getMonthUC: GetMonth,
    private val setModalityUC: SetModality,
    private val isYearOpenedUC: IsYearOpened
): ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState get() = _uiState.asStateFlow()


    /*
    private  var _monthDto : MutableStateFlow<MonthStatsDto> =
        MutableStateFlow(MonthStatsDto.emptyMonthDto())
    val monthDto: StateFlow<MonthStatsDto> get() = _monthDto

    private lateinit var _monthDays : MutableStateFlow<List<DayDto>>
    val monthDays: StateFlow<List<DayDto>> get() = _monthDays
*/
    fun initData(year: Int, month:Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (!isYearOpenedUC(year)) {
                    openYear(year)
                }
                loadMonth(year, month)
            }
        }
    }

    fun openYear(year: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            openYearUC(year)
        }
    }

    fun loadMonth(year: Int,  month: Int) {
        viewModelScope.launch(Dispatchers.IO) {

            val result = runCatching {
                val list = getMonthUC(year, month)
                val stats = MonthStatsDto(
                    year = year,
                    month = month,
                    days = list
                )
                stats
            }
            result.fold(
                onSuccess = {
                    _uiState.value = UiState.Success(it)
                    //_monthDto.value = it
                },
                onFailure = {exception ->
                    Log.e("[FMMP]", "Error while loading Month", exception)
                }
            )
        }
    }
/*
    fun loadMonth1(year: Int,  month: Int) {
        viewModelScope.launch(Dispatchers.IO) {

            val result: Result<Pair<List<DayDto>, MonthStatsDto>> = runCatching {
                val list = getMonthUC(year, month)
                val stats = MonthStatsDto(
                    year = year,
                    month = month,
                    days = list
                )
                list to stats
            }
            result.fold(
                onSuccess = { (list, stats) ->
                    _monthDays.value = list
                    _currentStats.value = stats
                },
                onFailure = {exception ->
                    Log.e("[FMMP]", "Error while loading Month", exception)
                }
            )
        }
    }

    fun loadMonth2(year: Int,  month: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val list = getMonthUC(year, month)
                val stats = monthStats(year, month, list)
                list to stats
            }.onSuccess { (list, stats) ->
                _monthDays.value = list
                _currentStats.value = stats
            }.onFailure { exception ->
                Log.e("[FMMP]", "Error while loading Month", exception)
            }

        }
    }
*/

/*
    fun toggleFestive(year: Int, date: LocalDate, festive: Boolean) {
        viewModelScope.launch {
            repo.toggleFestive(year, date, festive)
            loadMonth(year, date.month.number ) // Recarga mes
        }
    }
*/
    fun setModality(date: LocalDate, modality: Modality) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("[FMMP]", "setModality: $modality")
            setModalityUC(date, modality)
//            repo.setModality(date, modality)
            val year = date.year
            val month = date.month.number
            loadMonth(year, month)
        }
    }
}