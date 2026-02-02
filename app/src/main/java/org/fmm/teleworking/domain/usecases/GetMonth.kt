package org.fmm.teleworking.domain.usecases

import org.fmm.teleworking.domain.model.DayDto
import org.fmm.teleworking.domain.repository.ICalendarRepository
import javax.inject.Inject

class GetMonth @Inject constructor(private val calendarRepository: ICalendarRepository) {
    suspend operator fun invoke(year: Int, month:Int): List<DayDto> {
        if (!calendarRepository.isYearOpened(year))
            calendarRepository.openYear(year)
        return calendarRepository.getMonth(year, month)
    }
}