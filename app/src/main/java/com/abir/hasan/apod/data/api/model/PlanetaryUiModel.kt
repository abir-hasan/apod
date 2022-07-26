package com.abir.hasan.apod.data.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlanetaryUiModel(
    val title: String,
    val explanation: String,
    val date: String,
    val mediaType: String,
    val hdUrl: String?,
    val url: String
) : Parcelable