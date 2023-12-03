package com.rmd.business.blogapp.domain.model

data class UiState(
    val isSignInSuccessfull : Boolean = false,
    val signinError : String? = null,
    val currentUser: User? = null,
    val isLoading : Boolean = false,
    val blogs : List<Blog> = emptyList()
)