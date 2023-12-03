package com.rmd.business.blogapp.domain.model

data class User(
    val userId : String = "",
    val username : String? = null,
    val profilePictureUrl : String? = null
)