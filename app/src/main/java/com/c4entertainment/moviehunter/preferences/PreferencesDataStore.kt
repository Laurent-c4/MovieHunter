package com.c4entertainment.moviehunter.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.c4entertainment.moviehunter.MovieApp
import com.c4entertainment.moviehunter.domain.model.UserSettings
import com.c4entertainment.moviehunter.util.DarkThemeConfig
import com.c4entertainment.moviehunter.util.MovieTVFilterConfig
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

    private val TAG = "SettingsDataStore"



    //create a flow to listen to changes in the datastore
     val userSettings = datastore.data.map { preferences ->
        UserSettings(
            useGrid = preferences[USE_GRID] ?: false,
            useDynamicColor = preferences[USE_DYNAMIC_COLOR] ?: true,
            useFingerPrint = preferences[USE_FINGERPRINT] ?: true,
            darkThemeConfig  = preferences[DATA_THEME_CONFIG] ?: DarkThemeConfig.FOLLOW_SYSTEM,
            sort = preferences[SORT] ?: false,
            showVideos = preferences[SHOW_VIDEOS] ?: true,
            movieTV = preferences[MOVIE_TV_FILTER] ?: MovieTVFilterConfig.MOVIE,
            movieTVBackup = preferences[MOVIE_TV_BACKUP_FILTER] ?: MovieTVFilterConfig.MOVIE,
        )
    }

    fun toggleUseGrid(useGrid:Boolean) {
        scope.launch {
            datastore.edit { preferences ->
                preferences[USE_GRID] = useGrid
            }
        }
    }

    fun toggleShowVideos(showVideos:Boolean) {
        scope.launch {
            datastore.edit { preferences ->
                preferences[SHOW_VIDEOS] = showVideos
            }
        }
    }

    fun toggleSort(sort:Boolean) {
        scope.launch {
            datastore.edit { preferences ->
                preferences[SORT] = sort
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

    fun setMovieTvFilter(movieTvFilter: String) {
        scope.launch {
            datastore.edit { preferences ->
                preferences[MOVIE_TV_FILTER] = movieTvFilter
            }
        }
    }

    fun setMovieTvFilterBackup(movieTvFilter: String) {
        scope.launch {
            datastore.edit { preferences ->
                preferences[MOVIE_TV_BACKUP_FILTER] = movieTvFilter
            }
        }
    }

    companion object {
        private val USE_GRID = booleanPreferencesKey("use_grid_key")
        private val USE_DYNAMIC_COLOR = booleanPreferencesKey("use_dynamic_color_key")
        private val DATA_THEME_CONFIG = stringPreferencesKey("data_theme_config_key")
        private val USE_FINGERPRINT = booleanPreferencesKey("use_fingerprint_key")
        private val SORT = booleanPreferencesKey("sort_key")
        private val SHOW_VIDEOS = booleanPreferencesKey("show_videos_key")
        private val MOVIE_TV_FILTER = stringPreferencesKey("movie_tv_filter_key")
        private val MOVIE_TV_BACKUP_FILTER = stringPreferencesKey("movie_tv_backup_filter_key")

    }
}