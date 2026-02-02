package org.fmm.teleworking.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class YearConfigWithFestives(
    @Embedded
    val config: YearConfig,
    @Relation(
        parentColumn = "year",
        entityColumn = "year"
    )
    val festives: Set<YearFestive>
)
