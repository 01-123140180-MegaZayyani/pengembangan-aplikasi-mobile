package com.example.notesapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notesapp.viewmodel.NotesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    viewModel: NotesViewModel,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var titleError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "New Note",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("✕", fontSize = 18.sp, color = Color.White)
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            if (title.isBlank()) {
                                titleError = true
                            } else {
                                viewModel.addNote(title, content)
                                onBack()
                            }
                        }
                    ) {
                        Text(
                            "Save",
                            color = Color(0xFF6C63FF),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A1A2E)
                )
            )
        },
        containerColor = Color(0xFF1A1A2E)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Title field
            TextField(
                value = title,
                onValueChange = {
                    title = it
                    titleError = false
                },
                placeholder = {
                    Text(
                        "Note title...",
                        color = Color.White.copy(alpha = 0.3f),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = if (titleError) Color(0xFFFF6B6B) else Color(0xFF6C63FF),
                    unfocusedIndicatorColor = Color.White.copy(alpha = 0.1f),
                    cursorColor = Color(0xFF6C63FF)
                ),
                modifier = Modifier.fillMaxWidth(),
                isError = titleError,
                supportingText = if (titleError) {
                    { Text("Title cannot be empty", color = Color(0xFFFF6B6B)) }
                } else null
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Content field
            TextField(
                value = content,
                onValueChange = { content = it },
                placeholder = {
                    Text(
                        "Start writing your note...",
                        color = Color.White.copy(alpha = 0.3f),
                        fontSize = 15.sp
                    )
                },
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 15.sp,
                    color = Color.White,
                    lineHeight = 24.sp
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color(0xFF6C63FF)
                ),
                modifier = Modifier.fillMaxSize(),
                minLines = 10
            )
        }
    }
}
