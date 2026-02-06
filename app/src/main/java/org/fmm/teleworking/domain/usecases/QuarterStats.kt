package org.fmm.teleworking.domain.usecases

import org.fmm.teleworking.domain.model.stats.QuarterStatsDto
import org.fmm.teleworking.domain.model.stats.YearStatsDto
import org.fmm.teleworking.domain.repository.ICalendarRepository
import javax.inject.Inject

class QuarterStats @Inject constructor(private val calendarRepository: ICalendarRepository) {
    suspend operator fun invoke(year: Int, quarter: Int): QuarterStatsDto {

        val aux =
            if (calendarRepository.isYearOpened(year))
                calendarRepository.quarterStats(year, quarter)
            else
                emptyList()

        return QuarterStatsDto(year,quarter, aux)
    }
}