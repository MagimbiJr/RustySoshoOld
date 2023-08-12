package dev.rustybite.rustysosho.domain.model

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import java.io.IOException

data class Post(
    val postId: String = "",
    val postCaption: String = "",
    val postImage: String = "",
    val userId: String = "",
    val postedAt: Long = System.currentTimeMillis(),
    val isLiked: Boolean = false,
    val likes: Long = 0,
    val privacyStatus: String = ""
)

fun DocumentSnapshot.toPost(): Post? {
    return try {
        Post(
            postId = id,
            postCaption = getString("postCaption")!!,
            postImage = getString("postImage")!!,
            userId = getString("userId")!!,
            postedAt = getLong("postedAt")!!,
            isLiked = getBoolean("isLiked")!!,
            likes = getLong("likes")!!,
            privacyStatus = getString("privacyStatus")!!
        )
    } catch (e: FirebaseFirestoreException) {
        null
    } catch (e: IOException) {
        null
    }
}
