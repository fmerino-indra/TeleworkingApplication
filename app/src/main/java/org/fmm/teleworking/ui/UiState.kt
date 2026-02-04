package org.fmm.teleworking.ui

import org.fmm.teleworking.domain.model.MonthStatsDto

sealed class UiState {
    data object Loading: UiState()
    data object Idle: UiState()
    data class Success(val monthDto: MonthStatsDto) : UiState()
}