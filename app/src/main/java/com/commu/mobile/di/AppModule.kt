package com.commu.mobile.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    fun provideRetrofit():Retrofit{
        val baseUrlLocalRealDevice = "http://192.168.0.82:8080"
        return Retrofit.Builder()
            .baseUrl(baseUrlLocalRealDevice)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()

    }

}