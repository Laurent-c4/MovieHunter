package com.frogtest.movieguru.presentation.movies

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchWidget(
    text: String,
    onTextChange: (MovieEvent) -> Unit,
    onSearchClicked: (MovieEvent) -> Unit,
    onFilterClicked: () -> Unit,
    onDismiss: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .semantics {
                contentDescription = "SearchWidget"
            },
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        color = MaterialTheme.colorScheme.background
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(0.9f )
                    .semantics {
                        contentDescription = "TextField"
                    },
                value = text,
                onValueChange = { onTextChange(MovieEvent.OnSearchQueryChange(it)) },
                placeholder = {
                    Text(
                        modifier = Modifier
//                        .alpha(alpha = ContentAlpha.medium)
                        ,
                        text = "Search here...",
                    )
                },
//            textStyle = TextStyle(
//                color = MaterialTheme.colors.topAppBarContentColor
//            ),
                singleLine = true,
                leadingIcon = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon",
                        )
                    }
                },
                trailingIcon = {
                    IconButton(
                        modifier = Modifier
                            .semantics {
                                contentDescription = "CloseButton"
                            },
                        onClick = {
                            if (text.isNotEmpty()) {
                                onTextChange(MovieEvent.OnSearchQueryChange(""))
                            } else {
                                onDismiss()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Icon",)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearchClicked(MovieEvent.OnSearchInitiated)
                    }
                ),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
//                backgroundColor = Color.Transparent,
//                cursorColor = MaterialTheme.colors.topAppBarContentColor
                )
            )

            IconButton(onClick = { onFilterClicked() })
            {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filter",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

        }

    }
}

@Composable
@Preview
fun SearchWidgetPreview() {
    SearchWidget(
        text = "Search",
        onTextChange = {},
        onSearchClicked = {},
        onDismiss = {},
        onFilterClicked = {}
    )
}