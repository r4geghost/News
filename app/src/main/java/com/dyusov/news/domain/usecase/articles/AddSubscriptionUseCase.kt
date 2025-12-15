package com.dyusov.news.domain.usecase.articles

import com.dyusov.news.domain.repo.NewsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddSubscriptionUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(topic: String) {
        newsRepository.addSubscription(topic)
        // same as launch another coroutine in viewmodel
        CoroutineScope(currentCoroutineContext()).launch {
            newsRepository.updateArticlesForTopic(topic)
        }
    }
}