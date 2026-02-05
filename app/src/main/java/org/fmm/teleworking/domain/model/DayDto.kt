package org.fmm.teleworking.domain.model

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

data class DayDto (
    val date: LocalDate,
    val modality: Modality
) {
    fun isFestive(): Boolean = modality == Modality.FESTIVE
    fun isWeekend(): Boolean = modality == Modality.WEEKEND
    fun isTelework(): Boolean = modality == Modality.TELEWORK
    fun isPresential(): Boolean = modality == Modality.PRESENTIAL
    fun isHoliday(): Boolean = modality == Modality.HOLIDAY
    fun isTravel(): Boolean = modality == Modality.TRAVEL
    fun isUnassigned(): Boolean = modality == Modality.UNASSIGNED

    companion object {
        /*
            Porque sólo depende de la fecha
         */
        fun isWeekend(date: LocalDate): Boolean =
            date.dayOfWeek == DayOfWeek.SATURDAY || date.dayOfWeek == DayOfWeek.SUNDAY
        fun calcNumDaysByMonth(year: Int, month: Int): Int {
            val date = LocalDate(year=year, monthNumber =month, 1)
            val last = date.plus(1, DateTimeUnit.MONTH).minus(1, DateTimeUnit.DAY)
            return last.dayOfMonth
        }

        fun now(): LocalDate {
            return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        }
    }
}
