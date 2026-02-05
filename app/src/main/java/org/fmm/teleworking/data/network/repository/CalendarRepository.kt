package org.fmm.teleworking.data.network.repository

import org.fmm.teleworking.data.network.api.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalendarRepository @Inject constructor(
    private val api: ApiService) {
//): ICalendarRepository {
    /*
    override suspend fun openYear(year: Int) = api.openYear(OpenYearRequest(year))
    override suspend fun getMonth(year: Int, month:Int): List<DayDto> = api.getMonth(year, month)
    override suspend fun toggleFestive(year: Int, date: LocalDate, festive: Boolean)=
        api.toggleFestive (year, FestiveRequest(date = date, festive = festive))
    override suspend fun setModality(date: LocalDate, modality: Modality): DayDto =
        api.setModality(ModalityRequest(date, modality))
    override suspend fun quarterStats(year: Int,  q:Int): StatsDto =
        api.quarterStats(year, q)
    override suspend fun annualStats(year: Int): StatsDto =
        api.annualStats(year)

     */
}