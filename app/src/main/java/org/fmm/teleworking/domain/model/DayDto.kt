package org.fmm.teleworking.domain.model

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

data class DayDto (
    val date: LocalDate,
    val modality: Modality,
    val weekend: Boolean,
    val festive: Boolean=false
) {
    companion object {
        fun isWeekend(date: LocalDate): Boolean =
            date.dayOfWeek == DayOfWeek.SATURDAY || date.dayOfWeek == DayOfWeek.SUNDAY
    }
}
