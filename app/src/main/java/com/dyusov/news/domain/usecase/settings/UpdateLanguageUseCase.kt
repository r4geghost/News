package com.dyusov.news.domain.usecase.settings

import com.dyusov.news.domain.entity.Language
import com.dyusov.news.domain.repo.SettingsRepository
import javax.inject.Inject

class UpdateLanguageUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(language: Language) {
        return settingsRepository.updateLanguage(language)
    }
}
