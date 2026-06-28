package com.example.notesapp.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.notesapp.presentation.viewmodel.NotesViewModel
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    noteId: Long,
    viewModel: NotesViewModel,
    onNavigateBack: () -> Unit,
    onEditNote: (Long) -> Unit
) {
    val note by viewModel.selectedNote.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    val isSummarizing by viewModel.isSummarizing.collectAsState()
    val summaryResult by viewModel.summaryResult.collectAsState()
    val summaryError by viewModel.summaryError.collectAsState()
    var showSummaryDialog by remember { mutableStateOf(false) }

    LaunchedEffect(noteId) {
        viewModel.selectNote(noteId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Catatan") },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.clearSelectedNote()
                        onNavigateBack()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    note?.let { currentNote ->
                        IconButton(onClick = { viewModel.toggleFavorite(currentNote.id) }) {
                            Icon(
                                imageVector = if (currentNote.isFavorite)
                                    Icons.Default.Favorite
                                else
                                    Icons.Default.FavoriteBorder,
                                contentDescription = "Toggle favorit",
                                tint = if (currentNote.isFavorite)
                                    MaterialTheme.colorScheme.error
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    IconButton(onClick = { note?.let { onEditNote(it.id) } }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit catatan")
                    }
                    note?.let { currentNote ->
                        IconButton(
                            onClick = {
                                showSummaryDialog = true
                                viewModel.summarizeNote(currentNote.content)
                            }
                        ) {
                            Icon(
                                Icons.Default.AutoAwesome,
                                contentDescription = "Ringkas dengan AI",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Hapus catatan",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (note == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Text(
                    text = note!!.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Dibuat: ${formatDate(note!!.createdAt)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (note!!.updatedAt != note!!.createdAt) {
                    Text(
                        text = "Diperbarui: ${formatDate(note!!.updatedAt)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = note!!.content,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Hapus Catatan?") },
            text = { Text("Catatan ini akan dihapus permanen dan tidak bisa dikembalikan.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        note?.let { viewModel.deleteNote(it.id) }
                        viewModel.clearSelectedNote()
                        showDeleteDialog = false
                        onNavigateBack()
                    }
                ) {
                    Text("Hapus", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    if (showSummaryDialog) {
        AlertDialog(
            onDismissRequest = {
                showSummaryDialog = false
                viewModel.clearSummary()
            },
            title = {
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ringkasan AI")
                }
            },
            text = {
                when {
                    isSummarizing -> {
                        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Sedang meringkas catatan...")
                        }
                    }
                    summaryError != null -> {
                        Text(
                            text = summaryError ?: "Terjadi kesalahan",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    summaryResult != null -> {
                        Text(text = summaryResult ?: "")
                    }
                    else -> {
                        Text("Tidak ada ringkasan untuk ditampilkan.")
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSummaryDialog = false
                        viewModel.clearSummary()
                    }
                ) {
                    Text("Tutup")
                }
            },
            dismissButton = {
                if (summaryError != null && !isSummarizing) {
                    TextButton(
                        onClick = { note?.let { viewModel.summarizeNote(it.content) } }
                    ) {
                        Text("Coba Lagi")
                    }
                }
            }
        )
    }
}

private fun formatDate(epochMillis: Long): String {
    return try {
        val instant = Instant.fromEpochMilliseconds(epochMillis)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val day = localDateTime.dayOfMonth.toString().padStart(2, '0')
        val month = localDateTime.monthNumber.toString().padStart(2, '0')
        val year = localDateTime.year
        "$day-$month-$year"
    } catch (e: Exception) {
        "..."
    }
}
