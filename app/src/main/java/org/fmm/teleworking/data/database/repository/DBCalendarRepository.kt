package org.fmm.teleworking.data.database.repository

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
    override suspend fun openYear(year: Int): YearConfig = withContext(Dispatchers.IO) {
        val yearConfig = YearConfig(year)

        db.yearConfigDao().addYearConfig(yearConfig)

        val start = LocalDate(year=year, monthNumber =1, 1)
        val end = LocalDate(year=year, monthNumber =12, 31)
        var d = start;
        while (d <= end) {
            val day = db.domainDayDao().addDomainDay(DomainDay(date =d, modality =Modality
                .UNASSIGNED))
            d=d.plus(1, DateTimeUnit.DAY)
        }
        yearConfig
    }

    override suspend fun isYearOpened(year: Int): Boolean {
        val config = getYearConfig(year)
        return (config != null)
    }

    override suspend fun getYearConfig(year: Int): YearConfig? = withContext(Dispatchers.IO) {
        val config = db.yearConfigDao().findById(year)
        config

    }
    override suspend fun getMonth(year: Int, month:Int): List<DayDto>  = withContext(Dispatchers.IO) {
        val lists = db.domainDayDao().findAllDomainDay()
        lists.stream()
            .map {
                DayDto(
                    it.date,
                    it.modality,
                    DayDto.isWeekend(it.date),
                    false
                )}
            .collect(Collectors.toList())
    }
    /*
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