package com.rmd.business.blogapp.data.repository


import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rmd.business.blogapp.domain.model.User
import com.rmd.business.blogapp.ui.utils.Result
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepository @Inject constructor() {
    suspend fun signOut(oneTapClient: SignInClient) = flow {
        emit(Result.Loading)
        oneTapClient.signOut().await()
        Firebase.auth.signOut()
        emit(Result.Success(true))
    }.catch { error ->
        emit(Result.Error(error))
    }

    fun getSignedInUser() = flow {
        emit(Result.Loading)
        val fcu = Firebase.auth.currentUser
        val user = if (fcu != null) {
            User(fcu.uid, fcu.displayName, fcu.photoUrl.toString())
        } else null

        emit(Result.Success(user))
    }.catch { error ->
        emit(Result.Error(error))
    }
}