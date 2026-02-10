package org.fmm.teleworking.domain.repository

import kotlinx.datetime.LocalDate
import org.fmm.teleworking.data.model.YearConfig
import org.fmm.teleworking.domain.model.DayDto
import org.fmm.teleworking.domain.model.Modality
import javax.inject.Singleton

@Singleton
interface ICalendarRepository {
    suspend fun isYearOpened(year: Int): Boolean
    suspend fun openYear(year: Int): YearConfig
    suspend fun getOpenedYears(): List<YearConfig>
    suspend fun getYearConfig(year: Int): YearConfig?

    suspend fun getAllDays(): List<DayDto>

    suspend fun getMonth(year: Int, month:Int): List<DayDto>
    suspend fun setModality(date: LocalDate, modality: Modality):DayDto
    suspend fun yearStats(year: Int): List<DayDto>
    suspend fun quarterStats(year: Int, quarter: Int): List<DayDto>

}