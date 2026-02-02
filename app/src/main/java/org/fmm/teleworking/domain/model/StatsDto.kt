package org.fmm.teleworking.domain.model

data class StatsDto (
    val year: Int,
    val quarter: Int,
    val countsByModlity:
    Map<String, Int>,
    val totalDays: Int,
    val totalWorkddays: Int
)