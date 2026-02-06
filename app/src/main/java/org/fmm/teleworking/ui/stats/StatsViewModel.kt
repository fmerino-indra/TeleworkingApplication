package org.fmm.teleworking.ui.stats

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.fmm.teleworking.domain.model.stats.BasicStatsDto
import org.fmm.teleworking.domain.usecases.IsYearOpened
import org.fmm.teleworking.domain.usecases.QuarterStats
import org.fmm.teleworking.domain.usecases.YearStats
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
//    private val isYearOpenedUC: IsYearOpened,
    private val yearStatsUC: YearStats,
    private val quarterStatsUC: QuarterStats
): ViewModel() {
    private val _statsUiState = MutableStateFlow<StatsUIState>(StatsUIState.Idle)
    val statsUiState get() = _statsUiState.asStateFlow()

    fun initData() {
        _statsUiState.value = StatsUIState.Idle
    }
    fun loadYearStats(year: Int) {
        viewModelScope.launch(Dispatchers.IO) {

            val result = runCatching {
                val stats = yearStatsUC(year)
                stats
            }
            result.fold(
                onSuccess = {
                    _statsUiState.value = StatsUIState.Success(listOf(it))
                    //_monthDto.value = it
                },
                onFailure = {exception ->
                    Log.e("[FMMP]", "Error while loading Month", exception)
                }
            )
        }
    }

    fun loadQuarterStats (year: Int, quarter: Int) {
        viewModelScope.launch(Dispatchers.IO) {

            val result = runCatching {
                val stats = quarterStatsUC(year,quarter)
                stats
            }
            result.fold(
                onSuccess = {
                    _statsUiState.value = StatsUIState.Success(listOf(it))
                    //_monthDto.value = it
                },
                onFailure = {exception ->
                    Log.e("[FMMP]", "Error while loading Month", exception)
                }
            )
        }
    }

    fun loadQuarters(year:Int) {
        viewModelScope.launch(Dispatchers.IO) {

            val result = runCatching {
                val stats1 = quarterStatsUC(year,1)
                val stats2 = quarterStatsUC(year,2)
                val stats3 = quarterStatsUC(year,3)
                val stats4 = quarterStatsUC(year,4)
                listOf(stats1, stats2, stats3, stats4)
            }
            result.fold(
                onSuccess = {
                    _statsUiState.value = StatsUIState.Success(it)
                    //_monthDto.value = it
                },
                onFailure = {exception ->
                    Log.e("[FMMP]", "Error while loading Month", exception)
                }
            )
        }

    }
}