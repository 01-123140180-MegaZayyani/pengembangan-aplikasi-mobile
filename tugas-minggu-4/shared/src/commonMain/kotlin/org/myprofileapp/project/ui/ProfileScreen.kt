package org.myprofileapp.project.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.myprofileapp.project.data.ProfileUiState
import org.myprofileapp.project.ui.components.LabeledTextField

@Composable
fun ProfileScreen(
    uiState: ProfileUiState,
    onNameChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onLocationChange: (String) -> Unit,
    onEdit: () -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    onToggleDarkMode: () -> Unit
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Dark Mode")
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = uiState.profile.isDarkMode,
                    onCheckedChange = { onToggleDarkMode() }
                )
            }

            if (uiState.isEditing) {
                EditProfileForm(
                    nameInput = uiState.nameInput,
                    bioInput = uiState.bioInput,
                    emailInput = uiState.emailInput,
                    phoneInput = uiState.phoneInput,
                    locationInput = uiState.locationInput,
                    onNameChange = onNameChange,
                    onBioChange = onBioChange,
                    onEmailChange = onEmailChange,
                    onPhoneChange = onPhoneChange,
                    onLocationChange = onLocationChange,
                    onSave = onSave,
                    onCancel = onCancel
                )
            } else {
                ProfileHeader(
                    name = uiState.profile.name,
                    title = uiState.profile.title
                )
                
                ProfileInfoCard(
                    bio = uiState.profile.bio,
                    email = uiState.profile.email,
                    phone = uiState.profile.phone,
                    location = uiState.profile.location
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(onClick = onEdit) {
                    Text("Edit Profil")
                }
            }
        }
    }
}

@Composable
fun EditProfileForm(
    nameInput: String,
    bioInput: String,
    emailInput: String,
    phoneInput: String,
    locationInput: String,
    onNameChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onLocationChange: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        LabeledTextField(
            label = "Nama",
            value = nameInput,
            onValueChange = onNameChange
        )

        LabeledTextField(
            label = "Bio",
            value = bioInput,
            onValueChange = onBioChange
        )

        LabeledTextField(
            label = "Email",
            value = emailInput,
            onValueChange = onEmailChange
        )

        LabeledTextField(
            label = "Phone",
            value = phoneInput,
            onValueChange = onPhoneChange
        )

        LabeledTextField(
            label = "Location",
            value = locationInput,
            onValueChange = onLocationChange
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onSave,
                modifier = Modifier.weight(1f)
            ) {
                Text("Simpan")
            }

            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) {
                Text("Batal")
            }
        }
    }
}
