package org.fmm.teleworking.domain.usecases

import android.util.Log
import org.fmm.teleworking.data.model.YearConfig
import org.fmm.teleworking.domain.model.DayDto
import org.fmm.teleworking.domain.repository.ICalendarRepository
import javax.inject.Inject

class GetOpenedYears @Inject constructor(private val calendarRepository: ICalendarRepository) {
    suspend operator fun invoke(): List<YearConfig> {
        /*
                var aux = false
                val list = listOf(2021,2022,2023,2024,2025,2026,2027,2028)
                list.forEach { year ->
                    aux = calendarRepository.isYearOpened(year)
                    Log.i("[FMMP]", "$year is $aux")

                }

                val otraList = calendarRepository.getAllDays()
                Log.i("[FMMP]", "Hay $otraList.size dias creados")

         */
        return calendarRepository.getOpenedYears()
    }
}