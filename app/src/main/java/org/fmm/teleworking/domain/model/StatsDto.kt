package org.fmm.teleworking.domain.model

data class StatsDto (
    val year: Int,
    val quarter: Int=0,
    val month: Int=0,
    val countsByModality: Map<Modality, Int> = emptyMap<Modality,Int>(),
    val totalDays: Int,
    val totalWorkdays: Int
)