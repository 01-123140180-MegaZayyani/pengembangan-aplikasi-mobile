package org.myprofileapp.project

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import org.myprofileapp.project.ui.ProfileScreen
import org.myprofileapp.project.viewmodel.ProfileViewModel

@Composable
fun App(
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    MaterialTheme(
        colorScheme =
            if (uiState.profile.isDarkMode)
                darkColorScheme()
            else
                lightColorScheme()
    ) {
        ProfileScreen(
            uiState = uiState,
            onNameChange = viewModel::onNameChange,
            onBioChange = viewModel::onBioChange,
            onEmailChange = viewModel::onEmailChange,
            onPhoneChange = viewModel::onPhoneChange,
            onLocationChange = viewModel::onLocationChange,
            onEdit = viewModel::startEditing,
            onSave = viewModel::saveProfile,
            onCancel = viewModel::cancelEditing,
            onToggleDarkMode = viewModel::toggleDarkMode
        )
    }
}
