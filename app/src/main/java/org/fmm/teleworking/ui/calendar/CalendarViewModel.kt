package org.fmm.teleworking.ui.calendar

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number
import okhttp3.internal.concurrent.Task
import org.fmm.teleworking.data.model.YearConfig
import org.fmm.teleworking.domain.model.Modality
import org.fmm.teleworking.domain.model.stats.MonthStatsDto
import org.fmm.teleworking.domain.usecases.GetMonth
import org.fmm.teleworking.domain.usecases.GetOpenedYears
import org.fmm.teleworking.domain.usecases.IsYearOpened
import org.fmm.teleworking.domain.usecases.OpenYear
import org.fmm.teleworking.domain.usecases.SetModality
import org.fmm.teleworking.domain.usecases.YearStats
import org.fmm.teleworking.ui.UiState
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val openYearUC: OpenYear,
    private val getMonthUC: GetMonth,
    private val setModalityUC: SetModality,
    private val isYearOpenedUC: IsYearOpened,
    private val yearStatsUC: YearStats,
    private val getOpenedYearsUC: GetOpenedYears
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

    fun reset() {
        _uiState.value = UiState.Idle
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

    fun loadYearStats(year: Int) {
        viewModelScope.launch(Dispatchers.IO) {

            val result = runCatching {
                val stats = yearStatsUC(year)
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

    /*
    Esto obliga a llamarla así:
    lifecycleScope.launch {
        val estaAbierto = viewModel.isOpened(2025)
        if (estaAbierto) {...}
    }
    */

    /*
    suspend fun isYearOpened(year: Int): Boolean {
        return withContext(Dispatchers.IO) {
              isYearOpenedUC(year)
        }
    }
    */


    fun isYearOpened(year: Int): Boolean {
        return runBlocking(Dispatchers.IO) {
            val aux = isYearOpenedUC(year)
            aux
        }
    }

}