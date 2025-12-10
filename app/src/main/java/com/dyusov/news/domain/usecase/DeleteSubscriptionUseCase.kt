package com.dyusov.news.domain.usecase

import com.dyusov.news.domain.repo.NewsRepository
import javax.inject.Inject

class DeleteSubscriptionUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(topic: String) = newsRepository.removeSubscription(topic)
}