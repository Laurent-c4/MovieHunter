/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.frogtest.movieguru.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frogtest.movieguru.domain.repository.AuthRepository
import com.frogtest.movieguru.domain.repository.UserSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userDataRepository: UserSettingsRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {
    val settingsUiState: StateFlow<SettingsUiState> =
        userDataRepository.userSettings
            .map { userData ->
                SettingsUiState.Success(
                    settings = UserEditableSettings(
                        useGrid  = userData.useGrid,
                        useFingerPrint = userData.useFingerPrint,
                        useDynamicColor = userData.useDynamicColor,
                        darkThemeConfig = userData.darkThemeConfig,
                    ),
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = SettingsUiState.Loading,
            )

    val getSignedInUser get() = authRepository.getSignedInUser()

    fun toggleUseGrid(useFingerPrint: Boolean) {
        viewModelScope.launch {
            userDataRepository.useGrid(useFingerPrint)
        }
    }

    fun toggleUseFingerPrint(useFingerPrint: Boolean) {
        viewModelScope.launch {
            userDataRepository.useFingerPrint(useFingerPrint)
        }
    }

    fun updateDarkThemeConfig(darkThemeConfig: String) {
        viewModelScope.launch {
            userDataRepository.setDarkThemeConfig(darkThemeConfig)
        }
    }

    fun updateDynamicColorPreference(useDynamicColor: Boolean) {
        viewModelScope.launch {
            userDataRepository.setDynamicColorPreference(useDynamicColor)
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
        }
    }
}

/**
 * Represents the settings which the user can edit within the app.
 */
data class UserEditableSettings(
    val useGrid: Boolean,
    val useFingerPrint: Boolean,
    val useDynamicColor: Boolean,
    val darkThemeConfig: String,
)

sealed interface SettingsUiState {
    object Loading : SettingsUiState
    data class Success(val settings: UserEditableSettings) : SettingsUiState
}
