package org.myprofileapp.project.data

data class ProfileUiState(
    val profile: Profile = Profile(),
    val isEditing: Boolean = false,
    val nameInput: String = profile.name,
    val bioInput: String = profile.bio,
    val emailInput: String = profile.email,
    val phoneInput: String = profile.phone,
    val locationInput: String = profile.location
)
