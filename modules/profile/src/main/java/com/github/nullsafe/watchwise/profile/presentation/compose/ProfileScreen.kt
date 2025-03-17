package com.github.nullsafe.watchwise.profile.presentation.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import com.github.nullsafe.watchwise.compose.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var activeProfile by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profile",
                        style = AppTheme.typography.title3,
                        color = AppTheme.colors.type.secondary
                    )
                },
                colors = TopAppBarDefaults.largeTopAppBarColors().copy(
                    titleContentColor = AppTheme.colors.type.secondary,
                    containerColor = AppTheme.colors.theme.tintSelection
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(AppTheme.colors.background.default)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (activeProfile == null) {
                Text(
                    text = "Kein aktives Profil vorhanden",
                    style = AppTheme.typography.body,
                    color = AppTheme.colors.type.secondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Benutzername-Eingabefeld
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    placeholder = {
                        Text(
                            text = "Neues Profil",
                            color = AppTheme.colors.type.secondary
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = AppTheme.colors.background.card,
                        unfocusedContainerColor = AppTheme.colors.background.card,
                        cursorColor = AppTheme.colors.type.primary,
                        focusedTextColor = AppTheme.colors.type.primary,
                        unfocusedTextColor = AppTheme.colors.type.primary,
                        focusedIndicatorColor = AppTheme.colors.type.primary,
                        unfocusedIndicatorColor = AppTheme.colors.type.secondary
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions.Default
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Passwort-Eingabefeld
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = {
                        Text(
                            text = "Passwort",
                            color = AppTheme.colors.type.secondary
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = AppTheme.colors.background.card,
                        unfocusedContainerColor = AppTheme.colors.background.card,
                        cursorColor = AppTheme.colors.type.primary,
                        focusedTextColor = AppTheme.colors.type.primary,
                        unfocusedTextColor = AppTheme.colors.type.primary,
                        focusedIndicatorColor = AppTheme.colors.type.primary,
                        unfocusedIndicatorColor = AppTheme.colors.type.secondary
                    ),
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions.Default,
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, contentDescription = "Toggle Password Visibility")
                        }
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        if (username.isNotEmpty() && password.isNotEmpty()) {
                            activeProfile = username
                            username = ""
                            password = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppTheme.colors.theme.tint,
                        contentColor = AppTheme.colors.type.inverse
                    )
                ) {
                    Text("Profil erstellen")
                }
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = AppTheme.colors.background.card
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Aktives Profil",
                            style = AppTheme.typography.body,
                            color = AppTheme.colors.type.secondary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = activeProfile!!,
                            style = AppTheme.typography.title2,
                            color = AppTheme.colors.type.primary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = { /* Navigation zur Profilauswahl */ },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Profil ändern")
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { activeProfile = null },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.type.alert)
                        ) {
                            Text("Profil löschen")
                        }
                    }
                }
            }
        }
    }
}
