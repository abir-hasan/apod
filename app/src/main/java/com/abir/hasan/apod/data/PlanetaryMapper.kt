package com.abir.hasan.apod.data

import com.abir.hasan.apod.data.api.model.AstronomyPicture
import com.abir.hasan.apod.data.api.model.PlanetaryUiModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

class PlanetaryMapper @Inject constructor() :
    Function1<List<AstronomyPicture>, List<PlanetaryUiModel>> {

    companion object {
        const val UI_DATE_FORMAT = "dd/mm/yyyy"
        const val INPUT_DATE_FORMAT = "yyyy-mm-dd"
        const val VALID_MEDIA_TYPE = "image"
    }

    /**
     * Converting response model to ui model
     * Also making sure that we only take image type media
     * And ignore every other type
     */
    override fun invoke(list: List<AstronomyPicture>): List<PlanetaryUiModel> {
        return list.filter { it.mediaType == VALID_MEDIA_TYPE }.map {
            PlanetaryUiModel(
                title = it.title,
                explanation = it.explanation,
                date = parseDateTimeWithFormat(it.date),
                mediaType = it.mediaType,
                hdUrl = it.hdUrl,
                url = it.url
            )
        }
    }

    private fun parseDateTimeWithFormat(currentDate: LocalDate): String {
        return try {
            val inputFormat = SimpleDateFormat(INPUT_DATE_FORMAT, Locale.ENGLISH)
            val outputFormat = SimpleDateFormat(UI_DATE_FORMAT, Locale.getDefault())
            val dateValue: Date = inputFormat.parse(currentDate.toString()) as Date
            outputFormat.format(dateValue)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}