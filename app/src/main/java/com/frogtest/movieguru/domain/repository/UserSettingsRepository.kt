package com.frogtest.movieguru.domain.repository

import com.frogtest.movieguru.domain.model.UserSettings
import kotlinx.coroutines.flow.Flow

interface UserSettingsRepository {

    /**
     * Stream of [UserSettings]
     */
    val userSettings: Flow<UserSettings>

    /**
     * Sets the desired dark theme config.
     */
    suspend fun setDarkThemeConfig(darkThemeConfig: String)

    /**
     * Sets the preferred dynamic color config.
     */
    suspend fun setDynamicColorPreference(useDynamicColor: Boolean)

    /**
     * Sets whether user should view list or grid
     */
    suspend fun useGrid(useGrid: Boolean)

    /**
     * Sets whether user should use fingerprint
     */
    suspend fun useFingerPrint(useFingerPrint: Boolean)
}