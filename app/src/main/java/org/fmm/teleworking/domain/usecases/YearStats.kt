package org.fmm.teleworking.domain.usecases

import org.fmm.teleworking.domain.model.stats.YearStatsDto
import org.fmm.teleworking.domain.repository.ICalendarRepository
import javax.inject.Inject

class YearStats @Inject constructor(private val calendarRepository: ICalendarRepository) {
    suspend operator fun invoke(year: Int): YearStatsDto {

        val aux =
            if (calendarRepository.isYearOpened(year))
                calendarRepository.yearStats(year)
            else
                emptyList()

        return YearStatsDto(year, aux)
    }
}