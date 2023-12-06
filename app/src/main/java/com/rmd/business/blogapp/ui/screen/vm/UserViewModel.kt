package com.rmd.business.blogapp.ui.screen.vm

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.SignInClient
import com.rmd.business.blogapp.data.repository.UserRepository
import com.rmd.business.blogapp.domain.model.SignInResult
import com.rmd.business.blogapp.domain.model.UiState
import com.rmd.business.blogapp.ui.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    val uiState = mutableStateOf(UiState())

    init {
        getSignedInUser()
    }

    fun onSignInResult(result: SignInResult) {
        uiState.value = uiState.value.copy(
            isSignInSuccessfull = result.data != null,
            signinError = result.errorMessage,
            currentUser = result.data
        )
    }

    private fun getSignedInUser() {
        viewModelScope.launch {
            userRepository.getSignedInUser().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        uiState.value = uiState.value.copy(
                            isLoading = true
                        )
                    }

                    is Result.Success -> {
                        uiState.value = uiState.value.copy(
                            isLoading = false, currentUser = result.data
                        )
                    }

                    is Result.Error -> {
                        uiState.value = uiState.value.copy(
                            isLoading = false, currentUser = null, signinError = result.e?.message
                        )
                    }
                }
            }
        }
    }

    fun signOut(oneTapClient: SignInClient) {
        viewModelScope.launch {
            userRepository.signOut(oneTapClient).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        uiState.value = uiState.value.copy(
                            isLoading = true
                        )
                    }

                    is Result.Success -> {
                        uiState.value = uiState.value.copy(
                            isLoading = false, currentUser = null
                        )
                    }

                    is Result.Error -> {
                        uiState.value = uiState.value.copy(
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun resetSignInState() {
        uiState.value = uiState.value.copy(
            isSignInSuccessfull = false, signinError = null
        )
    }
}