package com.abir.hasan.apod.data

import com.abir.hasan.apod.data.api.model.AstronomyPicture
import com.abir.hasan.apod.data.api.model.PlanetaryUiModel
import com.abir.hasan.apod.util.BaseUnitTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import java.time.LocalDate

@ExperimentalCoroutinesApi
class PlanetaryMapperShould : BaseUnitTest() {

    private val picture1 = AstronomyPicture(
        serviceVersion = "1.0",
        title = "Picture 1",
        explanation = "Details 1",
        date = LocalDate.now(),
        mediaType = PlanetaryMapper.VALID_MEDIA_TYPE,
        hdUrl = "hd-url 1",
        url = "url 1"
    )

    private val picture2 = AstronomyPicture(
        serviceVersion = "1.0",
        title = "Picture 2",
        explanation = "Details 2",
        date = LocalDate.now(),
        mediaType = "video",
        hdUrl = "hd-url 2",
        url = "url 2"
    )

    private val picture3 = AstronomyPicture(
        serviceVersion = "1.0",
        title = "Picture 3",
        explanation = "Details 3",
        date = LocalDate.now(),
        mediaType = PlanetaryMapper.VALID_MEDIA_TYPE,
        hdUrl = "hd-url 3",
        url = "url 3"
    )

    private val mapper = PlanetaryMapper()
    private val responseList = listOf(picture1, picture2, picture3)

    @Test
    fun keepOnlyImageMediaTypePictures() {
        val filteredList = responseList.filter { it.mediaType == PlanetaryMapper.VALID_MEDIA_TYPE }
        val outputListFromMapper: List<PlanetaryUiModel> = mapper(responseList)
        assertEquals(filteredList.size, outputListFromMapper.size)
    }

    @Test
    fun convertDateTimeToCorrectUiFormat() {
        val giveDate = LocalDate.of(2022, 7, 19)
        val expectedDate = "19/07/2022"
        val inputList = listOf(
            AstronomyPicture(
                serviceVersion = "1.0",
                title = "Picture",
                explanation = "Details",
                date = giveDate,
                mediaType = PlanetaryMapper.VALID_MEDIA_TYPE,
                hdUrl = "hd-url",
                url = "url"
            )
        )
        val outputListFromMapper: List<PlanetaryUiModel> = mapper(inputList)
        assertEquals(expectedDate, outputListFromMapper[0].date)
    }

}