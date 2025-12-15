package com.dyusov.news.domain.usecase.articles

import com.dyusov.news.domain.entity.Article
import com.dyusov.news.domain.repo.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetArticlesByTopicsUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    operator fun invoke(topics: List<String>): Flow<List<Article>> = newsRepository.getArticlesByTopics(topics)
}