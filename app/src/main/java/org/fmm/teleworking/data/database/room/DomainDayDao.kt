package org.fmm.teleworking.data.database.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import org.fmm.teleworking.data.model.DomainDay

@Dao
interface DomainDayDao {
    @Query("SELECT * FROM DomainDay")
//    fun findAllDomainDay(): Flow<List<DomainDay>>
    fun findAllDomainDay(): List<DomainDay>

    @Insert()
    suspend fun addDomainDay(domainDay: DomainDay)

    @Update
    suspend fun updateDomainDay(domainDay: DomainDay)

    @Delete
    suspend fun removeDomainDay(domainDay: DomainDay)

}