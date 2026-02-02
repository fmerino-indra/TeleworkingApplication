package org.fmm.teleworking.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import org.fmm.teleworking.domain.model.Modality

@Entity("DomainDay")
class DomainDay (
    @PrimaryKey(autoGenerate = true)
    val idDomainDay: Int=0,
    @ColumnInfo("date")
    val date: LocalDate,
    @ColumnInfo("modality")
    val modality: Modality
) {

}
