package dev.rustybite.rustysosho.domain.repository

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import dev.rustybite.rustysosho.data.remote.PostsService
import dev.rustybite.rustysosho.data.repository.PostsRepository
import dev.rustybite.rustysosho.domain.model.Post
import dev.rustybite.rustysosho.domain.model.Response
import dev.rustybite.rustysosho.domain.model.User
import dev.rustybite.rustysosho.utils.Resource
import kotlinx.coroutines.flow.Flow

class PostsRepositoryImpl(
    private val api: PostsService
) : PostsRepository {
    override fun addPost(postCaption: String, uri: Uri?, privacyStatus: String): Flow<Resource<out Response>> {
        return api.addPost(postCaption, uri, privacyStatus = privacyStatus)
    }

    override fun getPost(postId: String): Flow<Resource<out Post>> {
        TODO("Not yet implemented")
    }

    override fun getPosts(): Flow<Resource<out List<Post>>> {
        TODO("Not yet implemented")
    }

    override fun editPost(postId: String): Flow<Resource<out Response>> {
        TODO("Not yet implemented")
    }

    override fun deletePost(postId: String): Flow<Resource<out Boolean>> {
        TODO("Not yet implemented")
    }

    override fun onPostLiked(postId: String): Flow<Resource<out Boolean>> {
        TODO("Not yet implemented")
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun getUser(): Flow<Resource<out User>> {
        return api.getUser()
    }

}