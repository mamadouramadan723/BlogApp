package com.rmd.business.blogapp.di

import android.content.Context
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.rmd.business.blogapp.ui.utils.GoogleAuthUiHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    fun providesSignedClient(@ApplicationContext context: Context): SignInClient {
        return Identity.getSignInClient(context)
    }

    @Provides
    fun providesGoogleAuthSignInClient(
        signInClient: SignInClient, @ApplicationContext context: Context
    ): GoogleAuthUiHelper {
        return GoogleAuthUiHelper(signInClient)
    }
}