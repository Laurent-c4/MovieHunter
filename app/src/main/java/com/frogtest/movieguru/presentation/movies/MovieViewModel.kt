package com.frogtest.movieguru.presentation.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.map
import com.frogtest.movieguru.data.cache.MovieEntity
import com.frogtest.movieguru.data.mappers.toMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    pager: Pager<Int, MovieEntity>
): ViewModel() {

    val moviePagingFlow = pager.flow.map { pagingData ->
        pagingData.map { movieEntity ->
            movieEntity.toMovie()
        }
    }
        .cachedIn(viewModelScope)

}