package dev.rustybite.rustysosho.data.repository

import android.net.Uri
import dev.rustybite.rustysosho.domain.model.Post
import dev.rustybite.rustysosho.domain.model.Response
import dev.rustybite.rustysosho.domain.model.User
import dev.rustybite.rustysosho.utils.Resource
import kotlinx.coroutines.flow.Flow

interface PostsRepository {
    fun addPost(
        postCaption: String,
        uri: Uri?,
        privacyStatus: String
    ): Flow<Resource<out Response>>

    fun getPost(postId: String): Flow<Resource<out Post>>

    fun getPosts(): Flow<Resource<out List<Post>>>

    fun editPost(postId: String): Flow<Resource<out Response>>

    fun deletePost(postId: String): Flow<Resource<out Boolean>>

    fun onPostLiked(postId: String): Flow<Resource<out Boolean>>

    fun getUser(): Flow<Resource<out User>>
}