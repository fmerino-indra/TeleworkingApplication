package org.fmm.teleworking.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("YearConfig")
data class YearConfig (
    @PrimaryKey(autoGenerate = false)
    val year: Int
)