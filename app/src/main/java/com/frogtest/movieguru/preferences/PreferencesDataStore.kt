package com.frogtest.movieguru.preferences

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.frogtest.movieguru.MovieApp
import com.frogtest.movieguru.domain.model.UserSettings
import com.frogtest.movieguru.util.DarkThemeConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsDataStore
@Inject
constructor(app: MovieApp) {

//    private val datastore: DataStore<Preferences> = app.createDataStore(name = "settings")

    private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private val datastore = app.datastore

    private val scope = CoroutineScope(Main)



    //create a flow to listen to changes in the datastore
     val userSettings = datastore.data.map { preferences ->
        UserSettings(
            useGrid = preferences[USE_GRID] ?: true,
            useDynamicColor = preferences[USE_DYNAMIC_COLOR] ?: true,
            useFingerPrint = preferences[USE_FINGERPRINT] ?: true,
            darkThemeConfig  = preferences[DATA_THEME_CONFIG] ?: DarkThemeConfig.FOLLOW_SYSTEM,

        )
    }

    fun toggleUseGrid(useGrid:Boolean) {
        scope.launch {
            datastore.edit { preferences ->
                preferences[USE_GRID] = useGrid
            }
        }
    }

    fun toggleUseFingerPrint(useFingerPrint:Boolean) {
        scope.launch {
            datastore.edit { preferences ->
                preferences[USE_FINGERPRINT] = useFingerPrint
            }
        }
    }

    fun toggleUseDynamicColor(useDynamicColor: Boolean) {
        scope.launch {
            datastore.edit { preferences ->
                preferences[USE_DYNAMIC_COLOR] = useDynamicColor
            }
        }
    }

    fun setDataThemeConfig(darkThemeConfig: String) {
        scope.launch {
            datastore.edit { preferences ->
                preferences[DATA_THEME_CONFIG] = darkThemeConfig
            }
        }
    }

    companion object {
        private val USE_GRID = booleanPreferencesKey("use_grid_key")
        private val USE_DYNAMIC_COLOR = booleanPreferencesKey("use_dynamic_color_key")
        private val DATA_THEME_CONFIG = stringPreferencesKey("data_theme_config_key")
        private val USE_FINGERPRINT = booleanPreferencesKey("use_fingerprint_key")

    }
}