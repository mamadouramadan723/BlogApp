package com.rmd.business.blogapp.ui.screen.vm

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rmd.business.blogapp.data.repository.BlogRepository
import com.rmd.business.blogapp.domain.model.UiState
import com.rmd.business.blogapp.domain.model.User
import com.rmd.business.blogapp.ui.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlogViewModel @Inject constructor(
    private val blogRepository: BlogRepository
) : ViewModel() {
    val uiState = mutableStateOf(UiState())

    init {
        viewModelScope.launch {
            blogRepository.getBlogs().collect { result ->
                when (result) {
                    is Result.Success -> {
                        uiState.value = uiState.value.copy(
                            blogs = result.data!!
                        )
                    }

                    else -> {}
                }
            }
        }
    }

    fun onAddBlog(
        title: String, content: String, thumbnail: Uri, user: User
    ) {
        viewModelScope.launch {
            blogRepository.addBlog(title, content, thumbnail, user).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        uiState.value = uiState.value.copy(isLoading = true)
                    }

                    else -> {
                        uiState.value = uiState.value.copy(isLoading = false)
                    }
                }
            }
        }
    }

    fun updateBlog(
        id: String, title: String, content: String, thumbnail: Uri
    ) {
        viewModelScope.launch {
            blogRepository.updateBlog(
                id, title, content, thumbnail
            ).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        uiState.value = uiState.value.copy(isLoading = true)
                    }

                    else -> {
                        uiState.value = uiState.value.copy(isLoading = false)
                    }
                }
            }
        }
    }

    fun deleteBlog(id: String) {
        viewModelScope.launch {
            blogRepository.deleteBlog(id).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        uiState.value = uiState.value.copy(isLoading = true)
                    }

                    else -> {
                        uiState.value = uiState.value.copy(isLoading = false)
                    }
                }
            }
        }
    }
}