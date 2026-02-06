package org.fmm.teleworking.ui.stats

import org.fmm.teleworking.domain.model.stats.BasicStatsDto

sealed class StatsUIState {
    data object Loading: StatsUIState()
    data object Idle: StatsUIState()
    data class Success(val listStatsDto: List<BasicStatsDto>) : StatsUIState()
}