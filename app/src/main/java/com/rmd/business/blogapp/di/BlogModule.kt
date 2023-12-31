package com.rmd.business.blogapp.di

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object BlogModule {

    @Provides
    fun providesBlogsRef() = Firebase.firestore.collection("Blogs")

    @Provides
    fun providesStorageRef() = Firebase.storage.reference
}