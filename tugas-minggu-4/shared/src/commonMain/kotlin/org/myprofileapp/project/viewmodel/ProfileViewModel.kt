package org.myprofileapp.project.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.myprofileapp.project.data.ProfileUiState

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> =
        _uiState.asStateFlow()

    fun onNameChange(newName: String) {
        _uiState.update {
            it.copy(nameInput = newName)
        }
    }

    fun onBioChange(newBio: String) {
        _uiState.update {
            it.copy(bioInput = newBio)
        }
    }

    fun onEmailChange(newEmail: String) {
        _uiState.update {
            it.copy(emailInput = newEmail)
        }
    }

    fun onPhoneChange(newPhone: String) {
        _uiState.update {
            it.copy(phoneInput = newPhone)
        }
    }

    fun onLocationChange(newLocation: String) {
        _uiState.update {
            it.copy(locationInput = newLocation)
        }
    }

    fun startEditing() {
        _uiState.update {
            it.copy(
                isEditing = true,
                nameInput = it.profile.name,
                bioInput = it.profile.bio,
                emailInput = it.profile.email,
                phoneInput = it.profile.phone,
                locationInput = it.profile.location
            )
        }
    }

    fun cancelEditing() {
        _uiState.update {
            it.copy(isEditing = false)
        }
    }

    fun saveProfile() {
        _uiState.update {
            it.copy(
                profile = it.profile.copy(
                    name = it.nameInput,
                    bio = it.bioInput,
                    email = it.emailInput,
                    phone = it.phoneInput,
                    location = it.locationInput
                ),
                isEditing = false
            )
        }
    }

    fun toggleDarkMode() {
        _uiState.update {
            it.copy(
                profile = it.profile.copy(
                    isDarkMode = !it.profile.isDarkMode
                )
            )
        }
    }
}
