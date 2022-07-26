package com.abir.hasan.apod.di

import com.abir.hasan.apod.BuildConfig
import com.abir.hasan.apod.data.api.PlanetaryService
import com.abir.hasan.apod.data.api.model.DayAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideLogger(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
    }

    @Provides
    fun provideHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    fun provideMoshi() = Moshi
        .Builder()
        .add(DayAdapter())
        .add(KotlinJsonAdapterFactory())
        .build()


    @Provides
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi) = Retrofit.Builder()
        .baseUrl(BuildConfig.NASA_BASE_URL)
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()


    @Provides
    fun agifyAPI(retrofit: Retrofit): PlanetaryService {
        return retrofit.create(PlanetaryService::class.java)
    }

}