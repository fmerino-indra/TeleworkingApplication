package org.fmm.teleworking.domain.model.stats

import org.fmm.teleworking.domain.model.DayDto
import org.fmm.teleworking.domain.model.Modality

abstract class BasicStatsDto (
    open val year: Int,
    open val totalMonths: Int,
    open val fromMonth: Int,
    open val untilMonth: Int,
    open val days: List<DayDto>
    ) {
    val countsByModality: Map<Modality, Int> get() =
        if (days.isNotEmpty()) {
            days.groupingBy { day ->
                if (DayDto.Companion.isWeekend(day.date))
                    Modality.WEEKEND
                else {
                    when {
                        day.isTelework() -> Modality.TELEWORK
                        day.isPresential() -> Modality.PRESENTIAL
                        day.isFestive() -> Modality.FESTIVE
                        day.isHoliday() -> Modality.HOLIDAY
                        day.isTravel() -> Modality.TRAVEL
                        day.isUnassigned() -> Modality.UNASSIGNED
                        else -> Modality.UNASSIGNED
                    }
                }
            }.eachCount()
        } else
            emptyMap()
    val totalDays: Int
        get() {
            var acum: Int = 0
            for (m in fromMonth..untilMonth) {
                acum += DayDto.Companion.calcNumDaysByMonth(year, m)
            }
            return acum
        }
    val totalWorkingdays: Int
        get() = totalDays - getWeekendCount() - getFestiveCount() - getHolidayCount()
    val totalNonWorkingdays: Int
        get() = totalDays - totalWorkingdays
    /*
        Working days
     */
    fun getTeleworkingCount(): Int = (this.countsByModality[Modality.TELEWORK] ?: 0)
    fun getPresentialCount(): Int = (countsByModality[Modality.PRESENTIAL] ?: 0)
    fun getUnassignedCount(): Int = (countsByModality[Modality.UNASSIGNED] ?: 0)
    fun getTravelCount(): Int = (countsByModality[Modality.TRAVEL] ?: 0)

    /*
        Non working days
     */
    fun getFestiveCount(): Int = (countsByModality[Modality.FESTIVE] ?: 0)
    fun getHolidayCount(): Int = (countsByModality[Modality.HOLIDAY] ?: 0)
    fun getWeekendCount(): Int = (countsByModality[Modality.WEEKEND] ?: 0)
/*
    companion object {
        fun emptyBasicDto(): BasicStatsDto {
            return BasicStatsDto(
                year = 0,
                totalMonths = 0,
                fromMonth = 0,
                untilMonth = 0,
                days = emptyList()
            )
        }
    }

 */
}