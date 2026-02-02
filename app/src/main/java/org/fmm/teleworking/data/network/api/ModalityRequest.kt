package org.fmm.teleworking.data.network.api

import kotlinx.datetime.LocalDate
import org.fmm.teleworking.domain.model.Modality

class ModalityRequest (val date: LocalDate, val modality: Modality)