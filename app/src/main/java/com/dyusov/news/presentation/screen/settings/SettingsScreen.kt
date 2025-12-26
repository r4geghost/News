@file:OptIn(ExperimentalMaterial3Api::class)

package com.dyusov.news.presentation.screen.settings

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dyusov.news.R
import com.dyusov.news.data.mapper.toReadableContent

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {
            viewModel.processCommand(SettingsCommand.ToggleNotificationsEnabled(it))
        }
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        textAlign = TextAlign.Center,
                        text = stringResource(R.string.settings)
                    )
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickable {
                                onBackClick()
                            },
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_to_main_screen)
                    )
                }
            )
        }
    ) { paddingValues ->

        val state by viewModel.state.collectAsState()

        Column(
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.Top
        ) {
            SettingsCard(
                title = stringResource(R.string.news_language),
                subtitle = stringResource(R.string.choose_language_for_news_search)
            ) {
                SettingsDropdown(
                    modifier = modifier.widthIn(max = 160.dp),
                    items = state.availableLanguages,
                    selectedItem = state.settings.language,
                    onItemSelected = {
                        viewModel.processCommand(SettingsCommand.UpdateLanguage(it))
                    },
                    itemAsString = {
                        it.toReadableContent()
                    }
                )
            }

            SettingsCard(
                title = stringResource(R.string.update_interval),
                subtitle = stringResource(R.string.how_often_to_update_news)
            ) {
                SettingsDropdown(
                    modifier = modifier.widthIn(max = 160.dp),
                    items = state.availableIntervals,
                    selectedItem = state.settings.interval,
                    onItemSelected = {
                        viewModel.processCommand(SettingsCommand.UpdateInterval(it))
                    },
                    itemAsString = {
                        it.toReadableContent()
                    }
                )
            }

            SettingsCard(
                title = stringResource(R.string.notifications),
                subtitle = stringResource(R.string.show_notifications_about_articles)
            ) {
                Switch(
                    checked = state.settings.notificationsEnabled,
                    onCheckedChange = { enabled ->
                        if (enabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            viewModel.processCommand(
                                SettingsCommand.ToggleNotificationsEnabled(enabled)
                            )
                        }
                    },
                    thumbContent = {
                        if (state.settings.notificationsEnabled) {
                            // icon when switch is checked
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = stringResource(R.string.switch_is_checked),
                                modifier = Modifier.size(SwitchDefaults.IconSize)
                            )
                        } else {
                            // icon when switch is not checked
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = stringResource(R.string.switch_is_not_checked),
                                modifier = Modifier.size(SwitchDefaults.IconSize)
                            )
                        }
                    }
                )
            }

            SettingsCard(
                title = "Update only via Wi-Fi",
                subtitle = "Save mobile data"
            ) {
                Switch(
                    checked = state.settings.wifiOnly,
                    onCheckedChange = { enabled ->
                        viewModel.processCommand(
                            SettingsCommand.ToggleWifiOnlyEnabled(enabled)
                        )
                    },
                    thumbContent = {
                        if (state.settings.wifiOnly) {
                            // icon when switch is checked
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = stringResource(R.string.switch_is_checked),
                                modifier = Modifier.size(SwitchDefaults.IconSize)
                            )
                        } else {
                            // icon when switch is not checked
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = stringResource(R.string.switch_is_not_checked),
                                modifier = Modifier.size(SwitchDefaults.IconSize)
                            )
                        }
                    }
                )
            }

        }
    }
}

@Composable
fun SettingsCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors().copy(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            SettingLabel(
                modifier = Modifier.widthIn(max = 172.dp),
                title = title,
                subtitle = subtitle
            )

            Spacer(modifier = Modifier.weight(1f))

            content()
        }
    }
}

@Composable
fun SettingLabel(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 1.25.em
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = subtitle,
            fontSize = 12.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 1.25.em,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun <T> SettingsDropdown(
    modifier: Modifier = Modifier,
    items: List<T>,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    itemAsString: @Composable (T) -> String
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        TextField(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true),
            value = itemAsString(selectedItem),
            onValueChange = {},
            readOnly = true, // this text field is used only to show items
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(itemAsString(item))
                    },
                    onClick = {
                        onItemSelected(item)
                        expanded = false // close list after choosing one item
                    }
                )
            }
        }
    }
}