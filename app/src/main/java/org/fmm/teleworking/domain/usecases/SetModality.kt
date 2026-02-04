package org.fmm.teleworking.domain.usecases

import kotlinx.datetime.LocalDate
import org.fmm.teleworking.domain.model.DayDto
import org.fmm.teleworking.domain.model.Modality
import org.fmm.teleworking.domain.repository.ICalendarRepository
import java.util.Date
import javax.inject.Inject

class SetModality @Inject constructor(private val calendarRepository: ICalendarRepository) {
    suspend operator fun invoke(date: LocalDate, modality: Modality): DayDto {
        val dto = DayDto(date, modality)
        calendarRepository.setModality(date, modality)
        return dto
    }
}