package com.github.nullsafe.watchwise.profile.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.nullsafe.watchwise.profile.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.State
import com.github.nullsafe.watchwise.core.data.datastore.UserPreferencesManager
import com.github.nullsafe.watchwise.core.session.UserSessionManager
import kotlinx.coroutines.flow.collectLatest


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userPreferencesManager: UserPreferencesManager
) : ViewModel() {

    private val _activeProfile = mutableStateOf<String?>(null)
    val activeProfile: State<String?> get() = _activeProfile

    val error = mutableStateOf<String?>(null)
    val loading = mutableStateOf(false)

    init {
        viewModelScope.launch {
            userPreferencesManager.userPreferencesFlow.collectLatest { prefs ->
                if (prefs.userName.isNotEmpty()) {
                    _activeProfile.value = prefs.userName
                    UserSessionManager.activeProfile = prefs.userName
                }
            }
        }
    }

    fun register(username: String, password: String) {
        viewModelScope.launch {
            loading.value = true
            val success = userRepository.register(username, password)
            loading.value = false
            if (success) {
                _activeProfile.value = username
                UserSessionManager.activeProfile = username
                userPreferencesManager.updateUserName(username)
                error.value = null
            } else {
                error.value = "Registration failed"
            }
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            loading.value = true
            val success = userRepository.login(username, password)
            loading.value = false
            if (success) {
                _activeProfile.value = username
                UserSessionManager.activeProfile = username
                userPreferencesManager.updateUserName(username)
                error.value = null
            } else {
                error.value = "Login failed"
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _activeProfile.value = null
            UserSessionManager.activeProfile = null
            userPreferencesManager.clearUserName()
            error.value = null
        }
    }

    fun updatePassword(username: String, newPassword: String) {
        viewModelScope.launch {
            loading.value = true
            val success = userRepository.update(username, newPassword)
            loading.value = false
            error.value = if (success) null else "Password update failed"
        }
    }

    fun delete(username: String) {
        viewModelScope.launch {
            loading.value = true
            val success = userRepository.delete(username)
            loading.value = false
            if (success) {
                _activeProfile.value = null
                UserSessionManager.activeProfile = null
                error.value = null
            } else {
                error.value = "Delete failed"
            }
        }
    }
}
