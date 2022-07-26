package com.abir.hasan.apod.data

import com.abir.hasan.apod.data.api.PlanetaryRemoteDataSource
import com.abir.hasan.apod.data.api.PlanetaryService
import com.abir.hasan.apod.data.api.model.AstronomyPicture
import com.abir.hasan.apod.util.BaseUnitTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import retrofit2.Response

@ExperimentalCoroutinesApi
class PlanetaryRemoteDataSourceShould : BaseUnitTest() {

    private val planetaryService: PlanetaryService = mock()
    private val astronomyPictureList = mock<List<AstronomyPicture>>()
    private val exception = RuntimeException("Something went wrong")

    @Test
    fun fetchAPODListFromService() = runTest {
        val remoteSource = PlanetaryRemoteDataSource(planetaryService)
        remoteSource.loadAstronomyPictureList().first()
        verify(planetaryService, times(1)).getPictures()
    }

    @Test
    fun convertListFromServiceToFlowAndEmitsThem() = runTest {
        val remoteSource = mockSuccessCase()
        assertEquals(
            Result.success(astronomyPictureList),
            remoteSource.loadAstronomyPictureList().first()
        )
    }

    @Test
    fun emitErrorResultWhenApiSendsError() = runTest {
        val remoteSource = mockErrorCase()
        assertEquals(
            exception,
            remoteSource.loadAstronomyPictureList().first().exceptionOrNull()
        )
    }

    private suspend fun mockSuccessCase(): PlanetaryRemoteDataSource {
        whenever(planetaryService.getPictures()).thenReturn(
            Response.success(
                astronomyPictureList
            )
        )
        return PlanetaryRemoteDataSource(planetaryService)
    }

    private suspend fun mockErrorCase(): PlanetaryRemoteDataSource {
        whenever(planetaryService.getPictures()).thenThrow(exception)
        return PlanetaryRemoteDataSource(planetaryService)
    }

}