package com.c4entertainment.moviehunter.presentation.verify_email

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.c4entertainment.moviehunter.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun VerifyEmailTopBar(
    title: String = stringResource(id = R.string.app_name),
    photoUrl: String? = null,
    onSettingsClicked: () -> Unit,
    navigateBack: () -> Unit
) {
    TopAppBar(
        title = { Text(text = title) },
        actions =
        {

            IconButton(onClick = onSettingsClicked)
            {
                if (photoUrl != null) {
                    AsyncImage(
                        model = photoUrl,
                        contentDescription = "Profile",
                        modifier = Modifier
                            .clip(shape = CircleShape),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Profile",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    )
}