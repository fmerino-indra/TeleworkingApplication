package org.fmm.teleworking.data.network.api

import kotlinx.datetime.LocalDate

data class FestiveRequest (
    val date: LocalDate,
    val festive: Boolean
)