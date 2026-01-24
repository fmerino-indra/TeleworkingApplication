package org.fmm.teleworking.repository

import org.fmm.teleworking.model.DayDto
import org.fmm.teleworking.model.FestiveRequest
import org.fmm.teleworking.model.Modality
import org.fmm.teleworking.model.ModalityRequest
import org.fmm.teleworking.model.OpenYearRequest
import org.fmm.teleworking.model.StatsDto
import org.fmm.teleworking.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalendarRepository @Inject constructor(
    private val api: ApiService
){
    suspend fun openYear(year: Int) = api.openYear(OpenYearRequest(year))
    suspend fun getMonth(year: Int, month:Int): List<DayDto> = api.getMonth(year, month)
    suspend fun toggleFestive(year: Int, date: String, festive: Boolean)=
        api.toggleFestive (year, FestiveRequest(date = date, festive = festive))
    suspend fun setModality(date: String, modality: Modality): DayDto =
        api.setModality(ModalityRequest(date, modality))
    suspend fun quarterStats(year: Int,  q:Int): StatsDto =
        api.quarterStats(year, q)
    suspend fun annualStats(year: Int): StatsDto =
        api.annualStats(year)
}