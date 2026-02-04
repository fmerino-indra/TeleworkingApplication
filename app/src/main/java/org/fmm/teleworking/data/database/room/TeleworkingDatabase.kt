package org.fmm.teleworking.data.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.fmm.teleworking.data.model.DomainDay
import org.fmm.teleworking.data.model.YearConfig
import org.fmm.teleworking.data.model.YearFestive
import org.fmm.teleworking.data.model.util.TeleworkingConverters

@Database(
    entities = [DomainDay::class, YearConfig::class, YearFestive::class],
    version =  3,
    exportSchema =  false
)
@TypeConverters(TeleworkingConverters::class)
abstract class TeleworkingDatabase: RoomDatabase() {
    abstract fun domainDayDao(): DomainDayDao
    abstract fun yearConfigDao(): YearConfigDao

    abstract fun yearFestiveDao(): YearFestiveDao
    companion object {
        private const val DATABASE_NAME = "teleworking_db"
        @Volatile
        private lateinit var INSTANCE: TeleworkingDatabase

        fun getInstance(context: Context): TeleworkingDatabase {
            if (!::INSTANCE.isInitialized)
                synchronized(this) {
                val instance = Room.databaseBuilder(
                                context,
                                TeleworkingDatabase::class.java,
                                DATABASE_NAME
                            ).fallbackToDestructiveMigration(true).build()
                INSTANCE = instance
            }
            return INSTANCE
        }

    }
}