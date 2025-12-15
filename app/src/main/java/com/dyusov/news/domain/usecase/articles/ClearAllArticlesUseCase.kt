package com.dyusov.news.domain.usecase.articles

import com.dyusov.news.domain.repo.NewsRepository
import javax.inject.Inject

class ClearAllArticlesUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(topics: List<String>) = newsRepository.clearAllArticles(topics)
}