package com.rmd.business.blogapp.data.repository

import android.net.Uri
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.storage.StorageReference
import com.rmd.business.blogapp.domain.model.Blog
import com.rmd.business.blogapp.domain.model.User
import com.rmd.business.blogapp.ui.utils.Result
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class BlogRepository @Inject constructor(
    private val blogsRef: CollectionReference,
    private val storageRef: StorageReference
) {

    fun getBlogs() = callbackFlow {
        val snapshotListener = blogsRef.orderBy("createdDate")
            .addSnapshotListener { snapshot, error ->
                val result = if (snapshot != null) {
                    Result.Success(snapshot.toObjects(Blog::class.java))
                } else {
                    Result.Error(error!!)
                }
                trySend(result)
            }
        awaitClose {
            snapshotListener.remove()
        }
    }

    fun addBlog(
        title: String,
        content: String,
        thumbnail: Uri,
        user: User
    ) = flow {
        emit(Result.Loading)
        val id = blogsRef.document().id

        val imageStorageRef = storageRef.child("images/$id.jpg")
        val downloadUrl = imageStorageRef.putFile(thumbnail).await().storage.downloadUrl.await()

        val blog = Blog(
            id = id, title = title, content = content, thumbnail = downloadUrl.toString(),
            isFavorite = false, user = user, createdDate = null
        )

        blogsRef.document(id).set(blog).await()

        emit(Result.Success(true))
    }.catch { error ->
        emit(Result.Error(error))
    }

    fun updateBlog(id: String, title: String, content: String, thumbnail: Uri) = flow {
        emit(Result.Loading)

        val imageStorageRef = storageRef.child("images/$id.jpg")
        val downloadUrl = imageStorageRef.putFile(thumbnail).await().storage.downloadUrl.await()

        blogsRef.document(id).update(
            "title", title, "content", content, "thumbnail", downloadUrl.toString()
        ).await()
        emit(Result.Success(true))
    }.catch { error ->
        emit(Result.Error(error))
    }

    fun deleteBlog(id: String) = flow {
        emit(Result.Loading)
        storageRef.child("images/$id.jpg").delete().await()
        blogsRef.document(id).delete().await()
        emit(Result.Success(true))
    }.catch { error ->
        emit(Result.Error(error))
    }

}