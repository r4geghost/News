package com.dyusov.news.data.repo

import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.dyusov.news.data.background.RefreshDataWorker
import com.dyusov.news.data.local.ArticleDbModel
import com.dyusov.news.data.local.NewsDao
import com.dyusov.news.data.local.SubscriptionDbModel
import com.dyusov.news.data.mapper.toDbModels
import com.dyusov.news.data.mapper.toEntities
import com.dyusov.news.data.remote.NewsApiService
import com.dyusov.news.domain.entity.Article
import com.dyusov.news.domain.entity.RefreshConfig
import com.dyusov.news.domain.repo.NewsRepository
import jakarta.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class NewsRepositoryImpl @Inject constructor(
    private val newsDao: NewsDao,
    private val newsApiService: NewsApiService,
    private val workManager: WorkManager
) : NewsRepository {

    override fun getAllSubscriptions(): Flow<List<String>> {
        return newsDao.getAllSubscriptions().map { subscriptions ->
            subscriptions.map {
                it.topic
            }
        }
    }

    override suspend fun addSubscription(topic: String) {
        newsDao.addSubscription(SubscriptionDbModel(topic))
    }

    override suspend fun updateArticlesForTopic(topic: String) {
        val articles = loadArticles(topic)
        newsDao.addArticles(articles)
    }

    override suspend fun removeSubscription(topic: String) {
        newsDao.deleteSubscription(SubscriptionDbModel(topic))
    }

    override suspend fun updateAllArticles() {
        // return current subscription collection from DB
        val subscriptions = newsDao.getAllSubscriptions().first()

        // load articles for topic async - each one in different coroutine
        coroutineScope {// parent coroutine
            subscriptions.forEach {
                launch {
                    updateArticlesForTopic(it.topic)
                }
            }
        }
    }

    override fun getArticlesByTopics(topics: List<String>): Flow<List<Article>> {
        return newsDao.getAllArticlesByTopics(topics).map {
            it.toEntities()
        }
    }

    override suspend fun clearAllArticles(topics: List<String>) {
        newsDao.deleteArticlesByTopics(topics)
    }

    private suspend fun loadArticles(topic: String): List<ArticleDbModel> {
        return try {
            newsApiService.loadArticles(topic).toDbModels(topic)
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            Log.e("NewsRepository", e.stackTraceToString())
            listOf() // return empty list if error occurred
        }
    }

    override fun startBackgroundRefresh(refreshConfig: RefreshConfig) {
        // add users settings using Constraints
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(if (refreshConfig.wifiOnly) NetworkType.UNMETERED else NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        val request = PeriodicWorkRequestBuilder<RefreshDataWorker>(
            repeatInterval = refreshConfig.interval.minutes.toLong(),
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        )
            .setConstraints(constraints) // apply constraints
            .build()

        workManager.enqueueUniquePeriodicWork(
            uniqueWorkName = "Refresh data",
            existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            request = request
        )
    }

}