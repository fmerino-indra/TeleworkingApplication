package org.fmm.teleworking.ui.stats

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.fmm.teleworking.domain.usecases.IsYearOpened
import org.fmm.teleworking.domain.usecases.YearStats
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
//    private val isYearOpenedUC: IsYearOpened,
    private val yearStatsUC: YearStats
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
                    _statsUiState.value = StatsUIState.Success(it)
                    //_monthDto.value = it
                },
                onFailure = {exception ->
                    Log.e("[FMMP]", "Error while loading Month", exception)
                }
            )
        }
    }

    fun loadQuarterStats (year: Int, quarter: Int) {

    }
}