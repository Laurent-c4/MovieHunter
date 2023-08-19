package com.frogtest.movieguru.presentation.movies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.frogtest.movieguru.domain.model.Movie
import com.frogtest.movieguru.ui.theme.MovieGuruTheme

@Composable
fun MovieGridItem(
    movie: Movie,
    modifier: Modifier = Modifier
) {
        Column(
            modifier = modifier
                .padding(start = 4.dp, end = 4.dp)
        ) {
            AsyncImage(
                model = movie.poster,
                contentDescription = movie.title,
                modifier = Modifier
                    .height(250.dp)
                )


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(1f)
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 3,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = movie.year,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(1f)
            .padding(4.dp),
        verticalArrangement = Arrangement.Bottom
    ) {

        Divider(
        )
    }

}

@Preview
@Composable
fun MovieGridItemPreview() {
    MovieGuruTheme {
        MovieGridItem(
            movie = Movie(
                imdbID = "tt0372784",
                title = "Batman Begins",
                year = "2005",
                poster = "https://m.media-amazon.com/images/M/MV5BMjUyNzRhOWItMTViOS00NGQ0LTg0MjktOGQ0YWU2MmM3Mzc3XkEyXkFqcGdeQXVyNDQwMTQ5ODk@._V1_SX300.jpg",
                type = "movie"
            )
        )
    }
}