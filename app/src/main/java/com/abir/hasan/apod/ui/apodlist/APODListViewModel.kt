package com.abir.hasan.apod.ui.apodlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.abir.hasan.apod.R
import com.abir.hasan.apod.data.PlanetaryMapper
import com.abir.hasan.apod.data.PlanetaryRepository
import com.abir.hasan.apod.data.api.model.PlanetaryUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class APODListViewModel @Inject constructor(
    private val repository: PlanetaryRepository
) : ViewModel() {

    val apodUiState: LiveData<APODListUiState>
        get() = filteredPictures.asLiveData()

    private val _sortTypeStateFlow = MutableStateFlow(SortType.INITIAL_RESULT)

    private val _loadingStateFlow = MutableStateFlow(false)

    private val _itemStreamFlow = MutableStateFlow<Result<List<PlanetaryUiModel>>>(
        Result.success(emptyList())
    )

    private val filteredPictures: StateFlow<APODListUiState> =
        combine(_itemStreamFlow, _sortTypeStateFlow, _loadingStateFlow) { result, type, loading ->
            val listItems = result.getOrNull()
            println("combine-> result = ${result.isSuccess}, type = $type, loading = $loading")
            return@combine if ((listItems == null || listItems.isEmpty()) && loading) {
                APODListUiState(isLoading = true, sortType = type)
            } else if (result.isSuccess) {
                val orderedList = getSortedList(listItems ?: emptyList(), type)
                APODListUiState(items = orderedList, sortType = type)
            } else {
                generateErrorState(result, type)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(300_000L),
            initialValue = APODListUiState(isLoading = true, sortType = SortType.INITIAL_RESULT)
        )

    private fun generateErrorState(
        result: Result<List<PlanetaryUiModel>>,
        type: SortType
    ): APODListUiState {
        val exception = result.exceptionOrNull()
        return if (exception is UnknownHostException) {
            APODListUiState(
                errorInfo = ErrorInfo(
                    errorTitle = R.string.no_network_connection,
                    errorMessage = R.string.please_check_your_network_connection_and_try_again,
                    hasNetwork = false
                ),
                sortType = type
            )
        } else {
            APODListUiState(
                errorInfo = ErrorInfo(
                    errorTitle = R.string.oops_something_went_wrong,
                    errorMessage = R.string.please_refresh_the_page_and_try_again,
                    hasNetwork = true
                ),
                sortType = type
            )
        }
    }

    fun refreshList() {
        if (filteredPictures.value.items.isNotEmpty()) return
        viewModelScope.launch {
            _loadingStateFlow.value = true
            repository.getAstronomyPictureList().collect {
                _loadingStateFlow.value = false
                _itemStreamFlow.value = it
            }
        }
    }

    fun setFiltering(sortType: SortType) {
        _sortTypeStateFlow.value = sortType
    }

    private fun getSortedList(
        list: List<PlanetaryUiModel>,
        sortType: SortType
    ): List<PlanetaryUiModel> {
        return when (sortType) {
            SortType.INITIAL_RESULT -> list
            SortType.REORDER_BY_DATE_DESCENDING -> list.sortedByDescending { parseDate(it.date) }
            SortType.REORDER_BY_TITLE_ASCENDING -> list.sortedBy { it.title }
        }
    }

    private fun parseDate(currentDate: String): Long {
        return try {
            val inputFormat = SimpleDateFormat(PlanetaryMapper.UI_DATE_FORMAT, Locale.getDefault())
            val dateValue: Date = inputFormat.parse(currentDate)
            dateValue.time
        } catch (e: Exception) {
            e.printStackTrace()
            0L
        }
    }

}

data class APODListUiState(
    val items: List<PlanetaryUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val sortType: SortType,
    val errorInfo: ErrorInfo? = null
)

data class ErrorInfo(
    val errorTitle: Int = R.string.oops_something_went_wrong,
    val errorMessage: Int = R.string.please_refresh_the_page_and_try_again,
    val hasNetwork: Boolean = true
)
