package com.example.giphyapp.di

import android.content.Context
import androidx.room.Room
import com.example.giphyapp.data.api.ApiKeyInterceptor
import com.example.giphyapp.data.api.GiphyApiService
import com.example.giphyapp.data.database.GifsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) //dependencies provided here will be used across the application
class AppModule {

    @Provides
    @Named("BASE_URL")
    fun provideBaseUrl(): String = "https://api.giphy.com/v1/"

    @Provides
    @Named("API_KEY")
    fun provideApiKey(): String = "r6F16GBdEMkeVtLp6472NDskhIpRdVL9"

    @Provides
    fun provideInterceptor(@Named("API_KEY") API_KEY: String): Interceptor =
        ApiKeyInterceptor(API_KEY)

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: Interceptor) =
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()


    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, @Named("BASE_URL") BASE_URL: String): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): GiphyApiService = retrofit.create(GiphyApiService::class.java)

    @Provides
    @Singleton
    fun provideDb(@ApplicationContext appContext: Context): GifsDatabase =
        Room.databaseBuilder(appContext, GifsDatabase::class.java, "deleted_gifs_database")
            .fallbackToDestructiveMigration()
            .build()

}