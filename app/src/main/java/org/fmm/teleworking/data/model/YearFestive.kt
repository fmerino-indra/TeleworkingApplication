package org.fmm.teleworking.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import kotlinx.datetime.LocalDate

@Entity(
    tableName = "YearFestive",
    primaryKeys =["year","festive"],
    foreignKeys =[
        ForeignKey(
            entity = YearConfig::class,
            parentColumns = ["year"],
            childColumns = ["year"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices =  [Index("year")]
)
data class YearFestive(
    val year: Int,
    val festive: LocalDate
)
