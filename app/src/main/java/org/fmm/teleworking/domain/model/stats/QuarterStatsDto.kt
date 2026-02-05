package org.fmm.teleworking.domain.model.stats

import org.fmm.teleworking.domain.model.DayDto

data class QuarterStatsDto  (
    override val year: Int,
    val quarter: Int,
    override val days: List<DayDto>

): BasicStatsDto (
    year = year,
    totalMonths = 3,
    fromMonth = (quarter-1)*3+1,
    untilMonth = (quarter-1)*3+3,
    days = days
){
    companion object {
        fun emptyYearDto(): BasicStatsDto {
            return QuarterStatsDto(
                year = 0,
                quarter = 1,
                days = emptyList()
            )
        }
    }
}