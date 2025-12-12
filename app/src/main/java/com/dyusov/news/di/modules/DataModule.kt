package com.dyusov.news.di.modules

import android.content.Context
import androidx.room.Room
import com.dyusov.news.data.local.NewsDao
import com.dyusov.news.data.local.NewsDatabase
import com.dyusov.news.data.remote.NewsApiService
import com.dyusov.news.data.repo.NewsRepositoryImpl
import com.dyusov.news.domain.repo.NewsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    @Singleton
    fun bindNewsRepository(impl: NewsRepositoryImpl): NewsRepository

    companion object {

        @Provides
        @Singleton
        fun provideApiService(retrofit: Retrofit): NewsApiService {
            return retrofit.create<NewsApiService>()
        }

        @Provides
        @Singleton
        fun provideRetrofit(converter: Converter.Factory): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://newsapi.org/")
                .addConverterFactory(converter)
                .build()
        }

        @Provides
        @Singleton
        fun provideConverterFactory(json: Json): Converter.Factory {
            return json.asConverterFactory(
                "application/json".toMediaType()
            )
        }

        @Provides
        @Singleton
        fun provideJson(): Json {
            return Json {
                ignoreUnknownKeys = true // ignore unknown json properties
                coerceInputValues = true // use default values for null json properties
            }
        }

        @Provides
        @Singleton
        fun provideNewsDatabase(@ApplicationContext context: Context): NewsDatabase {
            return Room.databaseBuilder(
                klass = NewsDatabase::class.java,
                name = "news",
                context = context,
            ).fallbackToDestructiveMigration(dropAllTables = true).build()
        }

        @Provides
        @Singleton
        fun provideNewsDao(newsDatabase: NewsDatabase): NewsDao = newsDatabase.newsDao()
    }
}