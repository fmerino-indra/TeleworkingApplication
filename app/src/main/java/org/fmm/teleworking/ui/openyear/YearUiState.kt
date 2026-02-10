package org.fmm.teleworking.ui.openyear

import org.fmm.teleworking.data.model.YearConfig

sealed class YearUiState {
    data object Loading: YearUiState()
    data object Idle: YearUiState()
    data class Success(val yearConfigList: List<YearConfig>) : YearUiState()
}