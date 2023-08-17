package com.frogtest.movieguru.presentation.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.map
import com.frogtest.movieguru.data.cache.entity.MovieEntity
import com.frogtest.movieguru.data.mappers.toMovie
import com.frogtest.movieguru.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    repository: MovieRepository
): ViewModel() {

    val getMovies = repository.getMovies().map { pagingData ->
        pagingData.map { movieEntity ->
            movieEntity.toMovie()
        }
    }
        .cachedIn(viewModelScope)

}