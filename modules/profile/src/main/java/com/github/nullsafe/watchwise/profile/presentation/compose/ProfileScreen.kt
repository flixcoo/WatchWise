package com.github.nullsafe.watchwise.profile.presentation.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.github.nullsafe.watchwise.compose.components.chips.ChipView
import com.github.nullsafe.watchwise.compose.components.chips.ChipViewStyle
import com.github.nullsafe.watchwise.compose.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    // State für Profil-Handling
    var username by remember { mutableStateOf("") }
    var selectedProfile by remember { mutableStateOf<String?>(null) }
    val profileList = remember { mutableStateListOf("Profil 1", "Profil 2") } // Dummy-Daten

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.largeTopAppBarColors().copy(
                    titleContentColor = AppTheme.colors.type.secondary,
                    containerColor = AppTheme.colors.theme.tintSelection
                ),
                title = {
                    Text(
                        text = "Profile",
                        style = AppTheme.typography.title3,
                        color = AppTheme.colors.type.secondary
                    )
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(AppTheme.colors.background.default)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Profil hinzufügen
            Text(
                text = "Neues Profil erstellen",
                style = AppTheme.typography.title3,
                color = AppTheme.colors.type.secondary
            )

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Profilname") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            Button(
                onClick = {
                    if (username.isNotEmpty()) {
                        profileList.add(username)
                        username = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Profil erstellen")
            }

            // Spacer für Trennung
            Spacer(modifier = Modifier.height(16.dp))

            // Bestehende Profile anzeigen
            Text(
                text = "Profile verwalten",
                style = AppTheme.typography.title3,
                color = AppTheme.colors.type.secondary
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(profileList) { profile ->
                    ProfileItem(
                        profileName = profile,
                        onDelete = { profileList.remove(profile) },
                        onSelect = { selectedProfile = profile }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Aktuelles Profil
            if (selectedProfile != null) {
                Text(
                    text = "Aktives Profil: $selectedProfile",
                    style = AppTheme.typography.title3,
                    color = AppTheme.colors.type.secondary
                )

                Button(
                    onClick = { profileList.remove(selectedProfile) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.type.alert)
                ) {
                    Text("Profil löschen")
                }
            }
        }
    }
}

@Composable
fun ProfileItem(
    profileName: String,
    onDelete: () -> Unit,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ChipView(
            text = profileName,
            style = ChipViewStyle.Inform,
            onClick = { onSelect() }
        )

        Button(
            onClick = onDelete,
            colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.type.alert)
        ) {
            Text("Löschen")
        }
    }
}
