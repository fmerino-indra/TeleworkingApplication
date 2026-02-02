package org.fmm.teleworking.data.database.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.fmm.teleworking.data.model.DomainDay
import org.fmm.teleworking.data.model.YearFestive

@Dao
interface YearFestiveDao {
    @Query("SELECT * FROM YearFestive")
    fun findAllDomainDay(): List<YearFestive>

    @Query("SELECT * FROM YearFestive WHERE year = :year")
    fun findByYear(year: Int): List<YearFestive>

    @Insert
    suspend fun addDomainDay(domainDay: YearFestive)

    @Update
    suspend fun updateDomainDay(domainDay: YearFestive)

    @Delete
    suspend fun removeDomainDay(domainDay: YearFestive)

}