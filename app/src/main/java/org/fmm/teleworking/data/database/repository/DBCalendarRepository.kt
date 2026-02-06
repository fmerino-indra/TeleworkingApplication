package org.fmm.teleworking.data.database.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import org.fmm.teleworking.data.database.room.TeleworkingDatabase
import org.fmm.teleworking.data.model.DomainDay
import org.fmm.teleworking.data.model.YearConfig
import org.fmm.teleworking.domain.model.DayDto
import org.fmm.teleworking.domain.model.Modality
import org.fmm.teleworking.domain.repository.ICalendarRepository
import java.util.stream.Collectors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DBCalendarRepository @Inject constructor(
    private val db: TeleworkingDatabase
): ICalendarRepository {
    suspend fun deleteYear(year: Int) {
        val yearConfig = db.yearConfigDao().findById(year)
        if (yearConfig != null) {
            val days = db.domainDayDao().findByYear(year)
            db.domainDayDao().removeDomainDaysByYear(year)
            db.yearConfigDao().removeYearConfig(yearConfig)
        }

    }
    override suspend fun openYear(year: Int): YearConfig = withContext(Dispatchers.IO) {
        val yearConfig = YearConfig(year)

        db.yearConfigDao().addYearConfig(yearConfig)

        val start = LocalDate(year=year, monthNumber =1, 1)
        val end = LocalDate(year=year, monthNumber =12, 31)
        var d = start;
        while (d <= end) {
            db.domainDayDao().addDomainDay(
                DomainDay(date =d,modality =
                    if (DomainDay.isWeekend(d)) Modality.WEEKEND
                    else Modality.UNASSIGNED
                )
            )
            /*
            val day = DomainDay(date =d,
                modality =
                    if (DomainDay.isWeekend(d)) Modality.WEEKEND
                    else Modality.UNASSIGNED
            )
            db.domainDayDao().addDomainDay(day)

             */
            d=d.plus(1, DateTimeUnit.DAY)
        }
        yearConfig
    }

    override suspend fun isYearOpened(year: Int): Boolean {
        val config = getYearConfig(year)
        return (config != null)
    }

    override suspend fun getYearConfig(year: Int): YearConfig? = withContext(Dispatchers.IO) {
        val config: YearConfig? = db.yearConfigDao().findById(year)
        config
    }
    override suspend fun getMonth(year: Int, month:Int): List<DayDto>  = withContext(Dispatchers.IO) {
        val lists = db.domainDayDao().findByYearAndMonth(year, month)
        lists.stream()
            .map {
                DayDto(
                    date = it.date,
                    modality = it.modality
                )}
            .collect(Collectors.toList())
    }
    override suspend fun setModality(date: LocalDate, modality: Modality): DayDto {
        val day = db.domainDayDao().findByDate(date)
        val modDay = day.copy(modality = modality)
        db.domainDayDao().updateDomainDay(modDay)

        return DayDto(
            date=date,
            modality = modDay.modality)
    }

    override suspend fun yearStats(year: Int): List<DayDto> =
        toDto(db.domainDayDao().findByYear(year))

    override suspend fun quarterStats(
        year: Int,
        quarter: Int
    ): List<DayDto> {
        var month = ((quarter-1)*3)+1
        Log.d("[FMMP]", "Quarter: $quarter")
        Log.d("[FMMP]", "Mont: $month")
        val month1 = toDto(db.domainDayDao().findByYearAndMonth(year, month))

        month = ((quarter-1)*3)+2
        Log.d("[FMMP]", "Mont: $month")
        val month2 = toDto(db.domainDayDao().findByYearAndMonth(year, month))

        month = ((quarter-1)*3)+3
        Log.d("[FMMP]", "Mont: $month")
        val month3 = toDto(db.domainDayDao().findByYearAndMonth(year, month))

        return month1 + month2 + month3

    }


    /*
    override suspend fun toggleFestive(year: Int, date: LocalDate, festive: Boolean)=
        api.toggleFestive (year, FestiveRequest(date = date, festive = festive))
    override suspend fun quarterStats(year: Int,  q:Int): StatsDto =
        api.quarterStats(year, q)
    override suspend fun annualStats(year: Int): StatsDto =
        api.annualStats(year)

     */
    private fun toDto(list: List<DomainDay>): List<DayDto> =
        list.stream()
            .map {
                DayDto(
                    date = it.date,
                    modality = it.modality
                )}
            .collect(Collectors.toList())

}