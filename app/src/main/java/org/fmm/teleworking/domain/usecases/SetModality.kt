package org.fmm.teleworking.domain.usecases

import kotlinx.datetime.LocalDate
import org.fmm.teleworking.domain.model.DayDto
import org.fmm.teleworking.domain.model.Modality
import org.fmm.teleworking.domain.repository.ICalendarRepository
import java.util.Date
import javax.inject.Inject

class SetModality @Inject constructor(private val calendarRepository: ICalendarRepository) {
    suspend operator fun invoke(date: LocalDate, modality: Modality): DayDto {
        return DayDto(date, modality, DayDto.isWeekend(date), false)
        /*
        calendarRepository.setModality(date, modality)


         */
    }
}