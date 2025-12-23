package com.dyusov.news.presentation.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dyusov.news.domain.entity.Interval
import com.dyusov.news.domain.entity.Language
import com.dyusov.news.domain.entity.Settings
import com.dyusov.news.domain.usecase.settings.GetSettingsUseCase
import com.dyusov.news.domain.usecase.settings.UpdateIntervalUseCase
import com.dyusov.news.domain.usecase.settings.UpdateLanguageUseCase
import com.dyusov.news.domain.usecase.settings.UpdateNotificationsEnabledUseCase
import com.dyusov.news.domain.usecase.settings.UpdateWifiOnlyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    getSettingsUseCase: GetSettingsUseCase,
    private val updateIntervalUseCase: UpdateIntervalUseCase,
    private val updateLanguageUseCase: UpdateLanguageUseCase,
    private val updateNotificationsEnabledUseCase: UpdateNotificationsEnabledUseCase,
    private val updateWifiOnlyUseCase: UpdateWifiOnlyUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<SettingsState>(SettingsState.Initial)

    val state = _state.asStateFlow()

    init {
        getSettingsUseCase()
            .onEach { settings ->
                _state.update {
                    SettingsState.SettingsConfig(settings = settings)
                }
            }.launchIn(viewModelScope)
    }

    fun processCommand(command: SettingsCommand) {
        viewModelScope.launch {
            when (command) {
                is SettingsCommand.UpdateInterval -> {
                    updateIntervalUseCase(command.interval)
                }

                is SettingsCommand.UpdateLanguage -> {
                    updateLanguageUseCase(command.language)
                }

                is SettingsCommand.ToggleNotificationsEnabled -> {
                    updateNotificationsEnabledUseCase(command.enabled)
                }

                is SettingsCommand.ToggleWifiOnlyEnabled -> {
                    updateWifiOnlyUseCase(command.enabled)
                }
            }
        }
    }
}

// commands
sealed interface SettingsCommand {

    data class UpdateLanguage(val language: Language) : SettingsCommand

    data class UpdateInterval(val interval: Interval) : SettingsCommand

    data class ToggleNotificationsEnabled(val enabled: Boolean) : SettingsCommand

    data class ToggleWifiOnlyEnabled(val enabled: Boolean) : SettingsCommand
}

// screen state
sealed interface SettingsState {

    data object Initial : SettingsState

    data class SettingsConfig(val settings: Settings) : SettingsState
}