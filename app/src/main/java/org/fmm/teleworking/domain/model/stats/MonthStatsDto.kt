package org.fmm.teleworking.domain.model.stats

import org.fmm.teleworking.domain.model.DayDto
import org.fmm.teleworking.domain.model.Modality

data class MonthStatsDto (
    override val year: Int,
    val month: Int,
    override val days: List<DayDto>

    ): BasicStatsDto(
    year = year,
    totalMonths = 1,
    fromMonth = month,
    untilMonth = month,
    days = days
    ) {
    companion object {
        fun emptyMonthDto(): MonthStatsDto {
            return MonthStatsDto(2026,1, emptyList<DayDto>())
        }
    }
}