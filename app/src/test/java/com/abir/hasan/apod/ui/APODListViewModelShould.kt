package com.abir.hasan.apod.ui

import com.abir.hasan.apod.data.PlanetaryMapper
import com.abir.hasan.apod.data.PlanetaryRepository
import com.abir.hasan.apod.data.api.model.PlanetaryUiModel
import com.abir.hasan.apod.ui.apodlist.APODListViewModel
import com.abir.hasan.apod.ui.apodlist.SortType
import com.abir.hasan.apod.util.BaseUnitTest
import com.abir.hasan.apod.util.captureValues
import com.abir.hasan.apod.util.getValueForTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class APODListViewModelShould : BaseUnitTest() {

    private val repository: PlanetaryRepository = mock()
    private val apodListMocked: List<PlanetaryUiModel> = mock()
    private val exception = RuntimeException("Something went wrong")
    private val picture1 = PlanetaryUiModel(
        title = "C",
        explanation = "Details 1",
        date = "31/01/1999",
        mediaType = PlanetaryMapper.VALID_MEDIA_TYPE,
        hdUrl = "hd-url 1",
        url = "url 1"
    )

    private val picture2 = PlanetaryUiModel(
        title = "B",
        explanation = "Details 2",
        date = "19/07/2022",
        mediaType = PlanetaryMapper.VALID_MEDIA_TYPE,
        hdUrl = "hd-url 2",
        url = "url 2"
    )

    private val picture3 = PlanetaryUiModel(
        title = "A",
        explanation = "Details 3",
        date = "12/12/2012",
        mediaType = PlanetaryMapper.VALID_MEDIA_TYPE,
        hdUrl = "hd-url 3",
        url = "url 3"
    )

    @Test
    fun getAPODListFromRepositoryWhenNoDataExists() = runTest {
        val viewModel = APODListViewModel(repository)
        viewModel.refreshList()
        verify(repository, times(1)).getAstronomyPictureList()
    }

    @Test
    fun emitAPODListFromRepository() = runTest {
        val viewModel = mockSuccessCase()
        viewModel.refreshList()
        viewModel.apodUiState.captureValues {
            println("data-> $values")
            // As the first state loading will be true and items should be empty
            assertEquals(true, values[0]?.isLoading)
            assertEquals(true, values[0]?.items?.isEmpty())
            // As next state if success scenario then loading will be false and there will be items
            // from repository
            assertEquals(false, values[1]?.isLoading)
            assertEquals(apodListMocked, values[1]?.items)
        }
    }

    @Test
    fun emitErrorFromRepository() = runTest {
        val viewModel = mockErrorCase()
        viewModel.refreshList()
        viewModel.apodUiState.captureValues {
            // As the first state loading will be true and items should be empty
            assertEquals(true, values[0]?.isLoading)
            // As next state if it is an error scenario then hide loader and check error info
            assertEquals(false, values[1]?.isLoading)
            assertEquals(true, values[1]?.items?.isEmpty())
            assertEquals(true, values[1]?.errorInfo != null)
        }
    }

    @Test
    fun reorderPicturesByDateInDescendingOrder() = runTest {
        val viewModel = mockDataToSort()
        val expectedData = listOf(picture2, picture3, picture1)
        viewModel.refreshList()
        viewModel.setFiltering(SortType.REORDER_BY_DATE_DESCENDING)
        assertEquals(expectedData, viewModel.apodUiState.getValueForTest()?.items)
    }

    @Test
    fun reorderPicturesByTitleInAscendingOrder() = runTest {
        val viewModel = mockDataToSort()
        val expectedData = listOf(picture3, picture2, picture1)
        viewModel.refreshList()
        viewModel.setFiltering(SortType.REORDER_BY_TITLE_ASCENDING)
        assertEquals(expectedData, viewModel.apodUiState.getValueForTest()?.items)
    }

    @Test
    fun resetPicturesOrder() = runTest {
        val viewModel = mockDataToSort()
        val expectedData = listOf(picture1, picture2, picture3)
        viewModel.refreshList()
        viewModel.setFiltering(SortType.INITIAL_RESULT)
        assertEquals(expectedData, viewModel.apodUiState.getValueForTest()?.items)
    }

    private fun mockDataToSort(): APODListViewModel {
        val actualData = listOf(picture1, picture2, picture3)
        whenever(repository.getAstronomyPictureList()).thenReturn(
            flow { emit(Result.success(actualData)) }
        )
        return APODListViewModel(repository)
    }

    private fun mockSuccessCase(): APODListViewModel {
        whenever(repository.getAstronomyPictureList()).thenReturn(
            flow {
                emit(Result.success(apodListMocked))
            }
        )
        return APODListViewModel(repository)
    }

    private fun mockErrorCase(): APODListViewModel {
        whenever(repository.getAstronomyPictureList()).thenReturn(
            flow {
                emit(Result.failure(exception))
            }
        )
        return APODListViewModel(repository)
    }

}