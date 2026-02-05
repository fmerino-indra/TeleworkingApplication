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
    suspend fun getMonth(year: Int, month:Int): List<DayDto>
/*
    suspend fun toggleFestive(year: Int, date: LocalDate, festive: Boolean)
    suspend fun setModality(date: LocalDate, modality: Modality): DayDto
    suspend fun quarterStats(year: Int,  q:Int): StatsDto
    suspend fun annualStats(year: Int): StatsDto
    suspend fun yearOpened(year: Int): YearConfig

 */
    suspend fun getYearConfig(year: Int): YearConfig?

    suspend fun setModality(date: LocalDate, modality: Modality):DayDto
    suspend fun yearStats(year: Int): List<DayDto>
    suspend fun quarterStats(year: Int, quarter: Int): List<DayDto>
}