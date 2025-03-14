package com.github.nullsafe.watchwise.profile.presentation.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.nullsafe.watchwise.compose.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    var username by remember { mutableStateOf("") }
    var activeProfile by remember { mutableStateOf("Standard-Profil") } // Dummy-Daten

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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Aktuelles Profil anzeigen
            Text(
                text = "Aktives Profil:",
                style = AppTheme.typography.title3,
                color = AppTheme.colors.type.secondary
            )

            Text(
                text = activeProfile,
                style = AppTheme.typography.title2,
                color = AppTheme.colors.type.primary
            )

            // Profil ändern
            Button(
                onClick = {
                    // Navigation zu einer Profilauswahl implementieren
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Profil ändern")
            }

            // Profil löschen
            Button(
                onClick = {
                    activeProfile = "Kein aktives Profil"
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.type.alert)
            ) {
                Text("Profil löschen")
            }

            // Falls kein Profil existiert, Erstellungsoption anzeigen
            if (activeProfile == "Kein aktives Profil") {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Neues Profil") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                Button(
                    onClick = {
                        if (username.isNotEmpty()) {
                            activeProfile = username
                            username = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Profil erstellen")
                }
            }
        }
    }
}
