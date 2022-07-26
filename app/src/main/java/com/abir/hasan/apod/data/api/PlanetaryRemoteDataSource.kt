package com.abir.hasan.apod.data.api

import com.abir.hasan.apod.data.api.model.AstronomyPicture
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PlanetaryRemoteDataSource @Inject constructor(private val apiService: PlanetaryService) {

    fun loadAstronomyPictureList(): Flow<Result<List<AstronomyPicture>>> {
        return flow {
            val response = apiService.getPictures()
            if (response.isSuccessful) {
                emit(Result.success(response.body()!!))
            } else {
                val errorMessage = "${response.errorBody()?.source()}"
                emit(Result.failure(RuntimeException(errorMessage)))
            }
        }.catch { exception -> emit(Result.failure(exception)) }
    }

}