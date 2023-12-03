package com.rmd.business.blogapp.ui.utils

sealed class Result<T>(val data: T? = null, val e: Throwable? = null) {
    data object Loading : Result<Nothing>()
    class Success<T>(data: T?) : Result<T>(data)
    class Error<T>(e: Throwable) : Result<T>(e = e)
}