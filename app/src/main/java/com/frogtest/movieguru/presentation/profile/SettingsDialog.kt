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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.frogtest.movieguru.R
import com.frogtest.movieguru.domain.model.UserSettings
import com.frogtest.movieguru.presentation.sign_in.UserProfile
import com.frogtest.movieguru.ui.theme.supportsDynamicTheming
import com.frogtest.movieguru.util.DarkThemeConfig

@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val settingsUiState by viewModel.settingsUiState.collectAsStateWithLifecycle()
    SettingsDialogImpl(
        userProfile = viewModel.getSignedInUser,
        onDismiss = onDismiss,
        settingsUiState = settingsUiState,
        onSignOut = viewModel::signOut,
        onToggleUseGrid = viewModel::toggleUseGrid,
        onToggleUseFingerprint = viewModel::toggleUseFingerPrint,
        onChangeDynamicColorPreference = viewModel::updateDynamicColorPreference,
        onChangeDarkThemePreference = viewModel::updateDarkThemeConfig,
    )
}

@Composable
fun SettingsDialogImpl(
    userProfile: UserProfile?,
    settingsUiState: SettingsUiState,
    supportDynamicColor: Boolean = supportsDynamicTheming(),
    onDismiss: () -> Unit,
    onSignOut: () -> Unit,
    onToggleUseGrid: (useGrid: Boolean) -> Unit,
    onToggleUseFingerprint: (useFingerPrint: Boolean) -> Unit,
    onChangeDynamicColorPreference: (useDynamicColor: Boolean) -> Unit,
    onChangeDarkThemePreference: (darkThemeConfig: String) -> Unit,
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
                Profile(userProfile)
                Divider(Modifier.padding(top = 8.dp))
                when (settingsUiState) {
                    SettingsUiState.Loading -> {
                        Text(
                            text = stringResource(R.string.loading),
                            modifier = Modifier.padding(vertical = 16.dp),
                        )
                    }

                    is SettingsUiState.Success -> {
                        FingerprintSwitch(
                            useFingerprint = settingsUiState.settings.useFingerPrint,
                            onToggleFingerprint = onToggleUseFingerprint
                        )
                        Divider(Modifier.padding(top = 8.dp))
                        SettingsPanel(
                            settings = settingsUiState.settings,
                            supportDynamicColor = supportDynamicColor,
                            onChangeDynamicColorPreference = onChangeDynamicColorPreference,
                            onChangeDarkThemeConfig = onChangeDarkThemePreference,
                            onToggleUseGrid = onToggleUseGrid,
                        )
                    }
                }
                Divider(Modifier.padding(top = 8.dp))
                LinksPanel()
            }
        },
        confirmButton = {
            Text(
                text = stringResource(R.string.sign_out_button_text),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable {
                        onSignOut()
                        onDismiss()
                    },
            )
        },
    )
}

@Composable
private fun Profile(userProfile: UserProfile?) {
    Row {
        if (userProfile?.photoUrl != null) {
            AsyncImage(
                model = userProfile.photoUrl,
                contentDescription = "Pic",
                modifier = Modifier
                    .size(48.dp)
                    .clip(shape = CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Profile",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .size(48.dp)
//                                .clip(shape = CircleShape)
                    .align(Alignment.CenterVertically)
            )
        }
        Column(
            Modifier
                .padding(start = 16.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = userProfile?.name ?: "",
                style = MaterialTheme.typography.labelLarge,
            )
            Text(
                text = userProfile?.email ?: "",
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}

@Composable
private fun FingerprintSwitch(
    useFingerprint: Boolean,
    onToggleFingerprint: (useFingerPrint: Boolean) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = stringResource(R.string.use_fingerprint))
        Spacer(Modifier.weight(1f))
        Switch(checked = useFingerprint, onCheckedChange = { onToggleFingerprint(!useFingerprint) })
    }
}


@Composable
private fun SettingsPanel(
    settings: UserEditableSettings,
    supportDynamicColor: Boolean,
    onChangeDynamicColorPreference: (useDynamicColor: Boolean) -> Unit,
    onChangeDarkThemeConfig: (darkThemeConfig: String) -> Unit,
    onToggleUseGrid: (useGrid: Boolean) -> Unit,
) {

//    SettingsDialogSectionTitle(text = stringResource(R.string.movie_listing))
//    Column(Modifier.selectableGroup()) {
//        SettingsDialogThemeChooserRow(
//            text = stringResource(R.string.grid),
//            selected = settings.useGrid,
//            onClick = { onToggleUseGrid(true) },
//        )
//        SettingsDialogThemeChooserRow(
//            text = stringResource(R.string.list),
//            selected = !settings.useGrid,
//            onClick = { onToggleUseGrid(false) },
//        )
//    }
    if (supportDynamicColor) {
        SettingsDialogSectionTitle(text = stringResource(R.string.dynamic_color_preference))
        Column(Modifier.selectableGroup()) {
            SettingsDialogThemeChooserRow(
                text = stringResource(R.string.dynamic_color_yes),
                selected = settings.useDynamicColor,
                onClick = { onChangeDynamicColorPreference(true) },
            )
            SettingsDialogThemeChooserRow(
                text = stringResource(R.string.dynamic_color_no),
                selected = !settings.useDynamicColor,
                onClick = { onChangeDynamicColorPreference(false) },
            )
        }
    }
    SettingsDialogSectionTitle(text = stringResource(R.string.dark_mode_preference))
    Column(Modifier.selectableGroup()) {
        SettingsDialogThemeChooserRow(
            text = stringResource(R.string.dark_mode_config_system_default),
            selected = settings.darkThemeConfig == DarkThemeConfig.FOLLOW_SYSTEM,
            onClick = { onChangeDarkThemeConfig(DarkThemeConfig.FOLLOW_SYSTEM) },
        )
        SettingsDialogThemeChooserRow(
            text = stringResource(R.string.dark_mode_config_light),
            selected = settings.darkThemeConfig == DarkThemeConfig.LIGHT,
            onClick = { onChangeDarkThemeConfig(DarkThemeConfig.LIGHT) },
        )
        SettingsDialogThemeChooserRow(
            text = stringResource(R.string.dark_mode_config_dark),
            selected = settings.darkThemeConfig == DarkThemeConfig.DARK,
            onClick = { onChangeDarkThemeConfig(DarkThemeConfig.DARK) },
        )
    }
}

@Composable
private fun SettingsDialogSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
    )
}

@Composable
fun SettingsDialogThemeChooserRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
        )
        Spacer(Modifier.width(8.dp))
        Text(text)
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
                TextLink(
                    text = stringResource(R.string.feedback),
                    url = FEEDBACK_URL,
                )
            }
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row {
                    TextLink(
                        text = stringResource(R.string.developer),
                        url = DEVELOPER_URL,
                    )
                }
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

private const val FEEDBACK_URL = "https://github.com/Laurent-c4/MovieGuru/issues"
private const val DEVELOPER_URL = "https://laurentj.netlify.app"
