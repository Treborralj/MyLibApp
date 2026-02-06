package com.example.mylib.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mylib.viewModel.AuthenticationViewModel

@Composable
fun SignupPage(navController: NavController, viewModel: AuthenticationViewModel){
    var username by remember{ mutableStateOf("") }
    var password by remember{mutableStateOf("")}
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember{ SnackbarHostState() }

    LaunchedEffect(uiState.signupSuccess){
        val success = uiState.signupSuccess
        if(success != null){
            snackbarHostState.showSnackbar(
                message = "Signup successful for ${success.username}",
                actionLabel = "Sign in"
            )
            navController.navigate("loginPage"){
                popUpTo("signupPage"){inclusive = true}
            }
            viewModel.clearSignupSuccess()
        }
    }

    Scaffold(
        snackbarHost = {SnackbarHost(hostState = snackbarHostState)}
    ) { paddingValues ->

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") }
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.signup(username, password)
                }, enabled = !uiState.loading
            ) {
                Text(if(uiState.loading)"Signing up..." else "Signup")
            }
        }
    }
}