package com.dyusov.news.domain.usecase.settings

import com.dyusov.news.domain.repo.SettingsRepository
import javax.inject.Inject

class UpdateNotificationsEnabledUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(enabled: Boolean) {
        return settingsRepository.updateNotificationsEnabled(enabled)
    }
}
