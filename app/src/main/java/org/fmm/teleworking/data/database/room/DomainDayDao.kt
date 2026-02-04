package org.fmm.teleworking.data.database.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.datetime.LocalDate
import org.fmm.teleworking.data.model.DomainDay

@Dao
interface DomainDayDao {
    @Query("SELECT * FROM DomainDay")
//    fun findAllDomainDay(): Flow<List<DomainDay>>
    fun findAllDomainDay(): List<DomainDay>

    @Query("SELECT * FROM DomainDay" +
            " WHERE year = :year")
    fun findByYear(year: Int): List<DomainDay>

    @Query("SELECT * FROM DomainDay" +
            " WHERE year = :year and month = :month")
    fun findByYearAndMonth(year: Int, month: Int): List<DomainDay>

    @Query("SELECT * FROM DomainDay" +
            " WHERE date = :date")
    fun findByDate(date: LocalDate): DomainDay

    @Insert()
    suspend fun addDomainDay(domainDay: DomainDay)

    @Update
    suspend fun updateDomainDay(domainDay: DomainDay)

    @Delete
    suspend fun removeDomainDay(domainDay: DomainDay)

    @Query("DELETE FROM DomainDay where year = :year")
    suspend fun removeDomainDaysByYear(year: Int)

}