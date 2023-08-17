/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.frogtest.movieguru.presentation.profile

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.frogtest.movieguru.R
import com.frogtest.movieguru.presentation.auth.UserData

@Composable
fun SettingsDialog(
    userData: UserData?,
    onDismiss: () -> Unit,
    onSignOut: () -> Unit,
    onToggleFingerprint: () -> Unit,
    useFingerprint: Boolean = false,
    //    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val configuration = LocalConfiguration.current

    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.widthIn(max = configuration.screenWidthDp.dp - 80.dp),
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = stringResource(R.string.settings_title),
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                Row {
                    if (userData?.photoUrl != null) {
                        AsyncImage(
                            model = userData?.photoUrl,
                            contentDescription = "Pic",
                            modifier = Modifier
                                .size(48.dp)
                                .clip(shape = CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Column(
                        Modifier
                            .padding(start = 16.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        Text(
                            text = userData?.name ?: "",
                            style = MaterialTheme.typography.labelLarge,
                        )
                        Text(
                            text = userData?.email ?: "",
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                }

                Divider(Modifier.padding(top = 8.dp))

//                FingerprintSwitch(useFingerprint, onToggleFingerprint)
//
//                Divider(Modifier.padding(top = 8.dp))


                LinksPanel()
            }
        },
        confirmButton = {
            Text(
                text = stringResource(R.string.sign_out_button_text),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { onSignOut() },
            )
        },
    )
}

@Composable
private fun FingerprintSwitch(useFingerprint: Boolean, onToggleFingerprint: () -> Unit) {
    Row (verticalAlignment = Alignment.CenterVertically) {
        Text(text = stringResource(R.string.use_fingerprint))
        Spacer(Modifier.weight(1f))
        Switch(checked = useFingerprint, onCheckedChange = { onToggleFingerprint() })
    }
}

@Composable
private fun LinksPanel() {
    Row(
        modifier = Modifier.padding(top = 16.dp),
    ) {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row {
//                TextLink(
//                    text = stringResource(string.brand_guidelines),
//                    url = BRAND_GUIDELINES_URL,
//                )
//                Spacer(Modifier.width(16.dp))
                TextLink(
                    text = stringResource(R.string.feedback),
                    url = FEEDBACK_URL,
                )
            }
        }
    }
}

@Composable
private fun TextLink(text: String, url: String) {
    val launchResourceIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    val context = LocalContext.current

    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clickable {
                ContextCompat.startActivity(context, launchResourceIntent, null)
            },
    )
}

private const val FEEDBACK_URL = "https://goo.gle/nia-app-feedback"
