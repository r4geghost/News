package com.dyusov.news.domain.usecase.settings

import com.dyusov.news.domain.repo.SettingsRepository
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke() = settingsRepository.getSettings()
}
