package com.dyusov.news.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query
import androidx.room.Transaction
import com.dyusov.news.domain.entity.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Query("SELECT * FROM subscriptions")
    fun getAllSubscriptions(): Flow<List<SubscriptionDbModel>>

    @Insert(onConflict = IGNORE)
    suspend fun addSubscription(subscriptionDbModel: SubscriptionDbModel)

    @Transaction
    @Delete // no need to write query + also cascade deletion
    suspend fun deleteSubscription(subscriptionDbModel: SubscriptionDbModel)

    @Query("SELECT * FROM articles WHERE topic IN (:topics) ORDER BY publishedAt DESC")
    fun getAllArticlesByTopics(topics: List<String>): Flow<List<Article>>

    @Insert(onConflict = IGNORE)
    suspend fun addArticles(articles: List<ArticleDbModel>)

    @Transaction
    @Query("DELETE FROM articles WHERE topic IN (:topics)")
    suspend fun deleteArticlesByTopics(topics: List<String>)
}