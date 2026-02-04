package org.fmm.teleworking.domain.model

data class MonthStatsDto (
    val year: Int,
    val month: Int,
    val days: List<DayDto>,

) {
    val countsByModality: Map<Modality, Int> = days.groupingBy { day ->
        if (DayDto.isWeekend(day.date))
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
    val totalDays: Int
        get() = DayDto.numDays(year, month)
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

    companion object {
        fun emptyMonthDto(): MonthStatsDto {
            return MonthStatsDto(0,0,emptyList())
        }
    }
}