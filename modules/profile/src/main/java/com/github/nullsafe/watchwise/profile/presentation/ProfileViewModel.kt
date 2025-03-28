package com.github.nullsafe.watchwise.profile.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.nullsafe.watchwise.profile.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.State


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _activeProfile = mutableStateOf<String?>(null)
    val activeProfile: State<String?> get() = _activeProfile

    val error = mutableStateOf<String?>(null)
    val loading = mutableStateOf(false)

    private val _registrationSuccess = mutableStateOf<Boolean?>(null)
    val registrationSuccess: State<Boolean?> get() = _registrationSuccess


    fun register(username: String, password: String) {
        viewModelScope.launch {
            loading.value = true
            val success = userRepository.register(username, password)
            loading.value = false
            _registrationSuccess.value = success
            if (success) {
                _activeProfile.value = username
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
                error.value = null
            } else {
                error.value = "Login failed"
            }
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
                error.value = null
            } else {
                error.value = "Delete failed"
            }
        }
    }


}
