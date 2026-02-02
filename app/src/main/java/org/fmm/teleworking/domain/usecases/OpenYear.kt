package org.fmm.teleworking.domain.usecases

import org.fmm.teleworking.domain.repository.ICalendarRepository
import javax.inject.Inject

class OpenYear @Inject constructor(private val calendarRepository: ICalendarRepository) {
    suspend operator fun invoke(year: Int) {
        if (!calendarRepository.isYearOpened(year))
            calendarRepository.openYear(year)
    }
}