package com.dyusov.news.domain.usecase

import com.dyusov.news.domain.repo.NewsRepository
import javax.inject.Inject

class UpdateSubscribedArticlesUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke() = newsRepository.updateAllArticles()
}