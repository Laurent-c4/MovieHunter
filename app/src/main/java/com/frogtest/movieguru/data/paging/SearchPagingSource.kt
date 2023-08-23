package com.frogtest.movieguru.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.frogtest.movieguru.data.mappers.toMovie
import com.frogtest.movieguru.data.mappers.toMovieEntity
import com.frogtest.movieguru.data.network.api.TMDBMovieAPI
import com.frogtest.movieguru.data.network.apiresponses.MoviesResponse
import com.frogtest.movieguru.domain.model.movie.Movie

class SearchPagingSource(
    private val movieAPI: TMDBMovieAPI,
    private val query: String
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val currentPage = params.key ?: 1
        return try {
            val response = movieAPI.searchMovies(query = query)
            val endOfPaginationReached = response.results?.isEmpty() == true
            if (response.results?.isNotEmpty() == true) {
                LoadResult.Page(
                    data = response.results?.map { it.toMovie() } ?: emptyList(),
                    prevKey = if (currentPage == 1) null else currentPage - 1,
                    nextKey = if (endOfPaginationReached) null else currentPage + 1
                )
            } else {
                LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition
    }

}