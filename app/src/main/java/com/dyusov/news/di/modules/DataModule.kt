package com.dyusov.news.di.modules

import android.content.Context
import androidx.room.Room
import com.dyusov.news.data.local.NewsDao
import com.dyusov.news.data.local.NewsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    companion object {

        @Provides
        @Singleton
        fun provideNewsDatabase(
            @ApplicationContext context: Context
        ): NewsDatabase {
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