package com.dyusov.news.domain.usecase.settings

import com.dyusov.news.domain.entity.Interval
import com.dyusov.news.domain.repo.SettingsRepository
import javax.inject.Inject

class UpdateIntervalUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(interval: Interval) {
        return settingsRepository.updateInterval(interval.minutes)
    }
}
