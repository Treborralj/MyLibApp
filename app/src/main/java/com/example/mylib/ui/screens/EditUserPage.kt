package com.example.mylib.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mylib.ui.navigation.Routes
import com.example.mylib.ui.util.uriToFile
import com.example.mylib.viewModel.EditUserViewModel
import com.example.mylib.viewModel.authentication.AuthenticationViewModel

@Composable
fun EditUserPage(
    navController: NavController,
    viewModel: EditUserViewModel,
    authViewModel: AuthenticationViewModel,
    currentUsername: String,
    currentBio: String
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var deletePassword by remember { mutableStateOf("") }

    var username by rememberSaveable(currentUsername) { mutableStateOf(currentUsername) }
    var bio by rememberSaveable(currentBio) { mutableStateOf(currentBio) }
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedImageUri = uri
    }

    val accountChanged = username.trim() != currentUsername.trim() || bio.trim() != currentBio.trim()
    val canChangePassword = oldPassword.isNotBlank() && newPassword.isNotBlank() && confirmPassword.isNotBlank()

    LaunchedEffect(uiState.successMessage, uiState.requiresRelogin) {
        val message = uiState.successMessage ?: return@LaunchedEffect

        snackbarHostState.showSnackbar(message)

        if (uiState.requiresRelogin) {
            authViewModel.logout()
            navController.navigate(Routes.Login.route) {
                popUpTo(0)
            }
        } else {
            navController.popBackStack()
        }

        viewModel.clearState()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding()
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Edit Profile",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Profile photo",
                        style = MaterialTheme.typography.titleMedium
                    )

                    OutlinedButton(
                        onClick = { imagePickerLauncher.launch("image/*") },
                        enabled = !uiState.isLoading,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Choose photo")
                    }

                    selectedImageUri?.let {
                        Text(
                            text = "Photo selected",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Button(
                            onClick = {
                                val file = uriToFile(context, it)
                                viewModel.updateProfilePicture(file)
                            },
                            enabled = !uiState.isLoading,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Upload photo")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Profile details",
                        style = MaterialTheme.typography.titleMedium
                    )

                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = bio,
                        onValueChange = { bio = it },
                        label = { Text("Bio") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            viewModel.updateAccount(
                                currentUsername = currentUsername,
                                username = username,
                                bio = bio
                            )
                        },
                        enabled = accountChanged && !uiState.isLoading,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save changes")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Change password",
                        style = MaterialTheme.typography.titleMedium
                    )

                    OutlinedTextField(
                        value = oldPassword,
                        onValueChange = { oldPassword = it },
                        label = { Text("Current password") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("New password") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirm new password") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            viewModel.updatePassword(oldPassword, newPassword, confirmPassword)
                        },
                        enabled = canChangePassword && !uiState.isLoading,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Update password")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.isLoading) {
                Text(
                    text = "Saving...",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            uiState.errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Delete account",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.error
                    )

                    Text(
                        text = "This action cannot be undone.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )

                    OutlinedTextField(
                        value = deletePassword,
                        onValueChange = { deletePassword = it },
                        label = { Text("Confirm password") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            showDeleteDialog = true
                        },
                        enabled = deletePassword.isNotBlank() && !uiState.isLoading,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Delete account")
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            OutlinedButton(
                onClick = { navController.popBackStack() },
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back")
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = {
                    authViewModel.logout()
                    navController.navigate(Routes.Login.route) {
                        popUpTo(0)
                    }
                },
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Log out")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showDeleteDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text("Delete account?")
            },
            text = {
                Text("This action cannot be undone.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        viewModel.deleteAccount(deletePassword)
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}