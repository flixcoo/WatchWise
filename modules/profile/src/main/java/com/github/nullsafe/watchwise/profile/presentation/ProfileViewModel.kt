package com.github.nullsafe.watchwise.profile.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.nullsafe.watchwise.profile.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.State
import com.github.nullsafe.watchwise.core.session.UserSessionManager


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _activeProfile = mutableStateOf<String?>(null)
    val activeProfile: State<String?> get() = _activeProfile

    val error = mutableStateOf<String?>(null)
    val loading = mutableStateOf(false)

    init {
        _activeProfile.value = UserSessionManager.activeProfile
    }

    fun register(username: String, password: String) {
        viewModelScope.launch {
            loading.value = true
            val success = userRepository.register(username, password)
            loading.value = false
            if (success) {
                _activeProfile.value = username
                UserSessionManager.activeProfile = username
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
                error.value = null
            } else {
                error.value = "Login failed"
            }
        }
    }

    fun logout() {
        _activeProfile.value = null
        UserSessionManager.activeProfile = null
        error.value = null
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
