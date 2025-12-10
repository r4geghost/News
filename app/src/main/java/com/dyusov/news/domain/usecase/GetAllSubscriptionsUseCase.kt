package com.dyusov.news.domain.usecase

import com.dyusov.news.domain.repo.NewsRepository
import javax.inject.Inject

class GetAllSubscriptionsUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    operator fun invoke() = newsRepository.getAllSubscriptions()
}