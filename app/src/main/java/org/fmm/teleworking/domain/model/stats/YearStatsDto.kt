package org.fmm.teleworking.domain.model.stats

import org.fmm.teleworking.domain.model.DayDto

data class YearStatsDto  (
    override val year: Int,
    override val days: List<DayDto>

): BasicStatsDto (
    year = year,
    totalMonths = 12,
    fromMonth = 1,
    untilMonth = 12,
    days = days
){
    companion object {
        fun emptyYearDto(): BasicStatsDto {
            return YearStatsDto(
                year = 0,
                days = emptyList()
            )
        }
    }
}