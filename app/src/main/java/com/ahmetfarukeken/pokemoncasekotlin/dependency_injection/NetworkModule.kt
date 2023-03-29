package com.ahmetfarukeken.pokemoncasekotlin.dependency_injection

import com.ahmetfarukeken.pokemoncasekotlin.service.IPokemonAPI
import com.ahmetfarukeken.pokemoncasekotlin.utils.APIConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideBaseUrl() = APIConstants.BASE_URL

    @Provides
    fun provideRetrofitInstance(BASE_URL: String): IPokemonAPI =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(IPokemonAPI::class.java)
}