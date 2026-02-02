package org.fmm.teleworking.data.model.util

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate

class TeleworkingConverters {
    @TypeConverter
    fun fromString(value: String?): LocalDate? =
        value?.let (LocalDate::parse)

    @TypeConverter
    fun toString(date: LocalDate?): String? =
        date?.toString()
}