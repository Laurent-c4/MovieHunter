package com.c4entertainment.moviehunter.domain.repository

import com.c4entertainment.moviehunter.domain.model.UserSettings
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

    /**
     * Sets whether movies should be sort
     */
    suspend fun toggleSort(sort: Boolean)

    suspend fun toggleShowVideos(showVideos: Boolean)

    suspend fun setMovieTV(movieTV: String)

    suspend fun setMovieTVBackup(movieTV: String)
}