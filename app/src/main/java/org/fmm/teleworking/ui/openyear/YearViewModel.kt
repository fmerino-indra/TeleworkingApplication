package org.fmm.teleworking.ui.openyear

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
class YearViewModel @Inject constructor(
    private val openYearUC: OpenYear,
    private val isYearOpenedUC: IsYearOpened,
    private val getOpenedYearsUC: GetOpenedYears
): ViewModel() {
    private val _yearUiState = MutableStateFlow<YearUiState>(YearUiState.Loading)
    val yearUiState get() = _yearUiState.asStateFlow()
    fun initData(year: Int, month:Int) {
    }

    fun reset() {
        _yearUiState.value = YearUiState.Idle
    }
    fun openYear(year: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            openYearUC(year)
            getOpenedYearsUC()
        }
    }

    /*
    Esto obliga a llamarla así:
    lifecycleScope.launch {
        val estaAbierto = viewModel.isOpened(2025)
        if (estaAbierto) {...}
    }
     */

    fun isYearOpened(year: Int): Boolean {
        return runBlocking(Dispatchers.IO) {
            val aux = isYearOpenedUC(year)
            aux
        }
    }

    fun getOpenedYears() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("[FMMP]", "getOpenedYears")


            val result = runCatching {
                val list = getOpenedYearsUC()
                list
            }
            result.fold(
                onSuccess = {
                    _yearUiState.value = YearUiState.Success(it)
                    //_monthDto.value = it
                },
                onFailure = {exception ->
                    Log.e("[FMMP]", "Error while loading YearConfig list", exception)
                }
            )

        }
    }
}