package com.abir.hasan.apod.data

import com.abir.hasan.apod.data.api.PlanetaryRemoteDataSource
import com.abir.hasan.apod.data.api.model.AstronomyPicture
import com.abir.hasan.apod.data.api.model.PlanetaryUiModel
import com.abir.hasan.apod.util.BaseUnitTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class PlanetaryRepositoryShould : BaseUnitTest() {

    private val remoteSource: PlanetaryRemoteDataSource = mock()
    private val mapper: PlanetaryMapper = mock()
    private val apodList = mock<List<PlanetaryUiModel>>()
    private val astronomyPictureList = mock<List<AstronomyPicture>>()
    private val exception = RuntimeException("Something went wrong")

    @Test
    fun getAstronomyPicturesFromRemoteSource() = runTest {
        val repository = PlanetaryRepository(remoteSource, mapper)
        repository.getAstronomyPictureList()
        verify(remoteSource, times(1)).loadAstronomyPictureList()
    }

    @Test
    fun delegateBusinessLogicToMapper() = runTest {
        val repository = mockSuccessfulCase()
        repository.getAstronomyPictureList().first()
        verify(mapper, times(1)).invoke(astronomyPictureList)
    }

    @Test
    fun emitMappedPlanetaryUiModelFromSource() = runTest {
        val repository = mockSuccessfulCase()
        assertEquals(Result.success(apodList), repository.getAstronomyPictureList().first())
    }

    @Test
    fun propagateErrors() = runTest {
        val repository = mockErrorCase()
        assertEquals(exception, repository.getAstronomyPictureList().first().exceptionOrNull())
    }

    private fun mockErrorCase(): PlanetaryRepository {
        whenever(remoteSource.loadAstronomyPictureList()).thenReturn(
            flow { emit(Result.failure(exception)) }
        )
        return PlanetaryRepository(remoteSource, mapper)
    }

    private fun mockSuccessfulCase(): PlanetaryRepository {
        whenever(remoteSource.loadAstronomyPictureList()).thenReturn(
            flow { emit(Result.success(astronomyPictureList)) }
        )
        whenever(mapper.invoke(astronomyPictureList)).thenReturn(apodList)
        return PlanetaryRepository(remoteSource, mapper)
    }
}