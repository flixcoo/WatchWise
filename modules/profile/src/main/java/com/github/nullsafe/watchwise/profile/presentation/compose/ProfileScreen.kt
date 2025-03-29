package com.github.nullsafe.watchwise.profile.presentation.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.nullsafe.watchwise.compose.theme.AppTheme
import com.github.nullsafe.watchwise.profile.R
import com.github.nullsafe.watchwise.profile.presentation.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = androidx.hilt.navigation.compose.hiltViewModel()
) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var showPasswordUpdateScreen by remember { mutableStateOf(false) }
    var newPassword by remember { mutableStateOf("") }
    var newPasswordVisible by remember { mutableStateOf(false) }

    val registrationSuccess = viewModel.registrationSuccess.value
    var usernameError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val activeProfile by viewModel.activeProfile
    val isLoading by viewModel.loading
    val error by viewModel.error

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

                if (usernameError != null) {
                    Text(
                        text = usernameError!!,
                        color = Color.Red,
                        style = AppTheme.typography.body,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

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
                        val icon = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(icon, contentDescription = "Toggle Password Visibility")
                        }
                    }
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp))
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    AppTheme.colors.background.ghost.copy(alpha = 0.9f),
                                    AppTheme.colors.background.ghost.copy(alpha = 0.5f)
                                )
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = AppTheme.colors.background.border,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(16.dp)
                ) {
                    Column {
                        RuleCheck(context.getString(R.string.rule1_profile), hasMinLength(password))
                        RuleCheck(context.getString(R.string.rule2_profile), hasUpperCase(password))
                        RuleCheck(context.getString(R.string.rule3_profile), hasLowerCase(password))
                        RuleCheck(context.getString(R.string.rule4_profile), hasDigit(password))
                        RuleCheck(context.getString(R.string.rule5_profile), hasSpecialChar(password))
                    }
                }

                if (passwordError != null) {
                    Text(
                        text = passwordError!!,
                        color = Color.Red,
                        style = AppTheme.typography.body,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            usernameError = null
                            passwordError = null

                            if (username.isEmpty()) {
                                usernameError = "Username must not be empty"
                            } else if (username.length < 5) {
                                usernameError = "Username must at least be 5 chars long"
                            }

                            if (password.isEmpty()) {
                                passwordError = "Password must not be empty"
                            } else if (!(
                                        hasMinLength(password) &&
                                                hasUpperCase(password) &&
                                                hasLowerCase(password) &&
                                                hasDigit(password) &&
                                                hasSpecialChar(password)
                                        )) {
                                passwordError = "Password does not meet all criteria"
                            }

                            if (usernameError == null && passwordError == null) {
                                viewModel.register(username, password)
                                username = ""
                                password = ""
                            }
                        },
                        enabled = !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppTheme.colors.type.ghost,
                            contentColor = AppTheme.colors.type.inverse
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = AppTheme.colors.type.inverse,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(context.getString(R.string.create_profile), style = AppTheme.typography.title2)
                        }
                    }

                    Button(
                        onClick = {
                            if (username.isNotEmpty() && password.isNotEmpty()) {
                                viewModel.login(username, password)
                                username = ""
                                password = ""
                            }
                        },
                        enabled = !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppTheme.colors.theme.tint.copy(alpha = 0.9f),
                            contentColor = AppTheme.colors.type.inverse
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = AppTheme.colors.type.inverse,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(context.getString(R.string.login_profile), style = AppTheme.typography.title2)
                        }
                    }
                }

                registrationSuccess?.let { success ->
                    val message = if (success) "registered successfully" else "registration failed"
                    val color = if (success) Color(0xFF4CAF50) else Color.Red

                    Text(
                        text = message,
                        color = color,
                        style = AppTheme.typography.body,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }

                if (error != null) {
                    Text(
                        text = error!!,
                        color = Color.Red,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }

            } else if (showPasswordUpdateScreen) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        placeholder = { Text(context.getString(R.string.new_password_profile), color = AppTheme.colors.type.secondary) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = AppTheme.colors.background.card,
                            unfocusedContainerColor = AppTheme.colors.background.card,
                            cursorColor = AppTheme.colors.type.primary,
                            focusedTextColor = AppTheme.colors.type.primary,
                            unfocusedTextColor = AppTheme.colors.type.primary,
                            focusedIndicatorColor = AppTheme.colors.type.primary,
                            unfocusedIndicatorColor = AppTheme.colors.type.secondary
                        ),
                        visualTransformation = if (newPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val icon = if (newPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            IconButton(onClick = { newPasswordVisible = !newPasswordVisible }) {
                                Icon(icon, contentDescription = "Toggle Password Visibility")
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                viewModel.updatePassword(activeProfile!!, newPassword)
                                newPassword = ""
                                showPasswordUpdateScreen = false
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.theme.tint)
                        ) {
                            Text(context.getString(R.string.confirm_profile))
                        }

                        Button(
                            onClick = {
                                newPassword = ""
                                showPasswordUpdateScreen = false
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        ) {
                            Text(context.getString(R.string.cancel_profile))
                        }
                    }
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
                            text = activeProfile ?: "–",
                            style = AppTheme.typography.title2,
                            color = AppTheme.colors.type.primary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = { showPasswordUpdateScreen = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF555555)
                            )
                        ) {
                            Text(context.getString(R.string.change_password))
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { viewModel.delete(activeProfile!!) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AppTheme.colors.type.alert.copy(alpha = 0.7f)
                            )
                        ) {
                            Text(context.getString(R.string.delete_profile))
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { viewModel.logout() },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AppTheme.colors.theme.tint.copy(alpha = 0.9f)
                            )
                        ) {
                            Text(context.getString(R.string.logout_profile))
                        }
                    }
                }
            }
        }
    }
}

fun hasMinLength(pw: String) = pw.length >= 8
fun hasUpperCase(pw: String) = pw.any { it.isUpperCase() }
fun hasLowerCase(pw: String) = pw.any { it.isLowerCase() }
fun hasDigit(pw: String) = pw.any { it.isDigit() }
fun hasSpecialChar(pw: String) = pw.any { "!@#\$%^&*()_+-=[]{}|;:'\",.<>?/".contains(it) }



