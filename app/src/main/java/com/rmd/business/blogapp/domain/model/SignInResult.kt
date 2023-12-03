package com.rmd.business.blogapp.domain.model

data class SignInResult(
    val data : User?,
    val errorMessage : String?
)