package com.frogtest.movieguru.preferences

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.frogtest.movieguru.MovieApp
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

    init {
        observeDataStore()
    }

    val useGrid = mutableStateOf(true)

    fun toggleUseGrid() {
        scope.launch {
            datastore.edit { preferences ->
                val current = preferences[USE_GRID] ?: false
                preferences[USE_GRID] = !current
            }
        }
    }

    private fun observeDataStore() {
        datastore.data.onEach { preferences ->
            preferences[USE_GRID]?.let { useFingerprint ->
                this.useGrid.value = useFingerprint
            }
        }.launchIn(scope)
    }

    companion object {
        private val USE_GRID = booleanPreferencesKey("use_grid_key")

    }
}