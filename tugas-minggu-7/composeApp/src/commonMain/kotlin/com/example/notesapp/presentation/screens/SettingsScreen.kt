package com.example.notesapp.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.notesapp.presentation.viewmodel.NotesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: NotesViewModel,
    onNavigateBack: () -> Unit
) {
    val currentTheme by viewModel.theme.collectAsState()
    val currentSortOrder by viewModel.sortOrder.collectAsState()
    val currentUserName by viewModel.userName.collectAsState()
    val currentViewStyle by viewModel.viewStyle.collectAsState()

    var userNameInput by remember(currentUserName) { mutableStateOf(currentUserName) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pengaturan") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            SettingsSection(title = "Tampilan") {
                SettingsItem(label = "Tema Aplikasi") {
                    ThemeSelector(
                        currentTheme = currentTheme,
                        onThemeChange = viewModel::changeTheme
                    )
                }

                SettingsItem(label = "Gaya Tampilan") {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("list" to "List", "grid" to "Grid").forEach { (value, label) ->
                            FilterChip(
                                selected = currentViewStyle == value,
                                onClick = {
                                    if (currentViewStyle != value) viewModel.toggleViewStyle()
                                },
                                label = { Text(label) }
                            )
                        }
                    }
                }
            }

            SettingsSection(title = "Urutan Catatan") {
                SettingsItem(label = "Urutkan berdasarkan") {
                    SortOrderSelector(
                        currentOrder = currentSortOrder,
                        onOrderChange = viewModel::changeSortOrder
                    )
                }
            }

            SettingsSection(title = "Profil") {
                SettingsItem(label = "Nama Tampilan") {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = userNameInput,
                            onValueChange = { userNameInput = it },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            placeholder = { Text("Masukkan namamu") }
                        )
                        Button(
                            onClick = { viewModel.changeUserName(userNameInput) },
                            enabled = userNameInput != currentUserName
                        ) {
                            Text("Simpan")
                        }
                    }
                }
            }

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Notes App — Tugas Minggu 7",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "SQLDelight + DataStore + KMP",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                content = content
            )
        }
    }
}

@Composable
private fun SettingsItem(
    label: String,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
        content()
    }
}

@Composable
private fun ThemeSelector(
    currentTheme: String,
    onThemeChange: (String) -> Unit
) {
    val themes = listOf(
        "system" to "Sistem",
        "light"  to "Terang",
        "dark"   to "Gelap"
    )
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        themes.forEach { (value, label) ->
            FilterChip(
                selected = currentTheme == value,
                onClick = { onThemeChange(value) },
                label = { Text(label) }
            )
        }
    }
}

@Composable
private fun SortOrderSelector(
    currentOrder: String,
    onOrderChange: (String) -> Unit
) {
    val orders = listOf(
        "newest"      to "Terbaru",
        "oldest"      to "Terlama",
        "alphabetical" to "Abjad"
    )
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        orders.forEach { (value, label) ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = currentOrder == value,
                    onClick = { onOrderChange(value) }
                )
                Text(label, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
