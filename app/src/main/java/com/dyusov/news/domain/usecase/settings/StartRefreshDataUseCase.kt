package com.dyusov.news.domain.usecase.settings

import com.dyusov.news.data.mapper.toRefreshConfig
import com.dyusov.news.domain.repo.NewsRepository
import com.dyusov.news.domain.repo.SettingsRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class StartRefreshDataUseCase @Inject constructor(
    private val newsRepositoryImpl: NewsRepository,
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke() {
        settingsRepository.getSettings()
            .map { it.toRefreshConfig() }
            .distinctUntilChanged()
            .onEach { newsRepositoryImpl.startBackgroundRefresh(it) }
            .collect()
    }
}