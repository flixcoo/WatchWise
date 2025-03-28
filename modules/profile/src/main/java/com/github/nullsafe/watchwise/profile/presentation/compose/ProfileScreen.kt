package com.github.nullsafe.watchwise.profile.presentation.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.github.nullsafe.watchwise.compose.theme.AppTheme
import com.github.nullsafe.watchwise.profile.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var activeProfile by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = context.getString(R.string.nav_profile),
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
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (activeProfile == null) {
                Text(
                    text = context.getString(R.string.no_profile),
                    style = AppTheme.typography.title2,
                    color = AppTheme.colors.type.secondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    placeholder = { Text(context.getString(R.string.username), color = AppTheme.colors.type.secondary) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
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
                    leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "Profile Icon") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text(context.getString(R.string.password), color = AppTheme.colors.type.secondary) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
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
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Password Icon") },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, contentDescription = "Toggle Password Visibility")
                        }
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (username.isNotEmpty() && password.isNotEmpty()) {
                            activeProfile = username
                            username = ""
                            password = ""
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppTheme.colors.theme.tint.copy(alpha = 0.9f),
                        contentColor = AppTheme.colors.type.inverse
                    )
                ) {
                    Text(context.getString(R.string.create_profile), style = AppTheme.typography.title2)
                }
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .shadow(8.dp, RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = AppTheme.colors.background.card)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Profile Picture",
                            tint = AppTheme.colors.type.primary,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(AppTheme.colors.background.ghost)
                                .padding(12.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = context.getString(R.string.active_profile),
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
                            onClick = { /* Navigate to profile selection */ },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF555555)
                            )
                        ) {
                            Text(context.getString(R.string.change_profile))
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { activeProfile = null },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AppTheme.colors.type.alert.copy(alpha = 0.7f)
                            )
                        ) {
                            Text(context.getString(R.string.delete_profile))
                        }
                    }
                }
            }
        }
    }
}
