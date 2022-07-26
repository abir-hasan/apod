package com.abir.hasan.apod.data

import com.abir.hasan.apod.data.api.PlanetaryRemoteDataSource
import com.abir.hasan.apod.data.api.model.PlanetaryUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlanetaryRepository @Inject constructor(
    private val remoteDataSource: PlanetaryRemoteDataSource,
    private val mapper: PlanetaryMapper
) {

    fun getAstronomyPictureList(): Flow<Result<List<PlanetaryUiModel>>> {
        return remoteDataSource.loadAstronomyPictureList().map {
            if (it.isSuccess)
                Result.success(mapper.invoke(it.getOrNull() ?: emptyList()))
            else
                Result.failure(it.exceptionOrNull()!!)
        }
    }

}