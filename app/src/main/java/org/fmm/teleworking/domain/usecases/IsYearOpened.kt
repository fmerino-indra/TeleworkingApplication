package org.fmm.teleworking.domain.usecases

import jakarta.inject.Inject
import org.fmm.teleworking.domain.repository.ICalendarRepository

class IsYearOpened @Inject constructor(private val calendarRepository: ICalendarRepository) {
    suspend operator fun invoke(year: Int): Boolean {
        return calendarRepository.isYearOpened(year)
    }
}