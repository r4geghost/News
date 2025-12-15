package com.dyusov.news.domain.usecase.settings

import com.dyusov.news.domain.repo.SettingsRepository
import javax.inject.Inject

class UpdateWifiEnabledUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(enabled: Boolean) {
        return settingsRepository.updateWifiEnabled(enabled)
    }
}
