package com.arvind.assistant.useCases.settings

import androidx.datastore.preferences.core.Preferences
import com.arvind.assistant.domain.repository.SettingsRepository

class GetSettingsUseCase (
    private val settingsRepository: SettingsRepository
) {
    operator fun <T> invoke(key: Preferences.Key<T>, defaultValue: T) = settingsRepository.getSettings(key, defaultValue)
}