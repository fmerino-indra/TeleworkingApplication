package org.fmm.teleworking.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import org.fmm.teleworking.domain.model.Modality

@Entity(
    tableName = "DomainDay",
    foreignKeys =[
        ForeignKey(
            entity = YearConfig::class,
            parentColumns = ["year"],
            childColumns = ["year"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices =  [Index(value=["year", "month"], unique = false )]
)
data class DomainDay (
    @PrimaryKey(autoGenerate = false)
    val date: LocalDate,
    @ColumnInfo("modality")
    val modality: Modality,
    @ColumnInfo("year")
    val year: Int = date.year,
    @ColumnInfo("month")
val month: Int = date.monthNumber

) {

    fun isFestive(): Boolean = modality == Modality.FESTIVE
    fun isWeekend(): Boolean = modality == Modality.WEEKEND
    fun isTelework(): Boolean = modality == Modality.TELEWORK
    fun isPresential(): Boolean = modality == Modality.PRESENTIAL
    fun isHoliday(): Boolean = modality == Modality.HOLIDAY
    fun isTravel(): Boolean = modality == Modality.TRAVEL
    fun isUnassigned(): Boolean = modality == Modality.UNASSIGNED

    companion object {
        fun isWeekend(date: LocalDate): Boolean =
            date.dayOfWeek == DayOfWeek.SATURDAY || date.dayOfWeek == DayOfWeek.SUNDAY
    }
}
