package org.fmm.teleworking.data.database.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.fmm.teleworking.data.model.YearConfig

@Dao
interface YearConfigDao {
    @Query("SELECT * FROM YearConfig")
    fun findAllYearConfig(): List<YearConfig>

    @Query("SELECT * FROM YearConfig WHERE year = :id")
    fun findById(id: Int): YearConfig?

    @Insert
    suspend fun addYearConfig(yearConfig: YearConfig)

    @Update
    suspend fun updateYearConfig(yearConfig: YearConfig)

    @Delete
    suspend fun removeYearConfig(yearConfig: YearConfig)

}