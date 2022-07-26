package com.abir.hasan.apod.data.api

import com.abir.hasan.apod.BuildConfig
import com.abir.hasan.apod.data.api.model.AstronomyPicture
import retrofit2.Response
import retrofit2.http.GET


interface PlanetaryService {
    /**
     * APOD - Astronomy Picture of the day.
     * See [the docs](https://api.nasa.gov/) and
     * See [github micro service](https://github.com/nasa/apod-api#docs-)
     */
    @GET("planetary/apod?count=20&api_key=${BuildConfig.API_KEY}")
    suspend fun getPictures(): Response<List<AstronomyPicture>>

}
