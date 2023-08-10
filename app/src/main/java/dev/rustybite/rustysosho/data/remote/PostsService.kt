package dev.rustybite.rustysosho.data.remote

import android.content.Context
import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import dev.rustybite.rustysosho.R
import dev.rustybite.rustysosho.domain.model.Post
import dev.rustybite.rustysosho.domain.model.Response
import dev.rustybite.rustysosho.utils.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.UUID

class PostsService(
    database: FirebaseFirestore,
    storage: FirebaseStorage,
    private val auth: FirebaseAuth,
    private val context: Context
) {
    private val postsRef = database.collection("posts")
    private val storageRef = storage.reference.child("media")

    fun addPost(
        postCaption: String,
        uri: Uri?
    ): Flow<Resource<out Response>> = callbackFlow {
        var snapshotResult: Task<Void>? = null
        var uploadSnapshot: Task<UploadTask.TaskSnapshot>? = null

        if (auth.currentUser != null) {
            if (uri != null) {
                uploadSnapshot = storageRef.putFile(uri).addOnCompleteListener { uploadTask ->
                    if (uploadTask.isSuccessful) {
                        if (uploadTask.isComplete) {
                            storageRef.downloadUrl.addOnSuccessListener { downloadedUri ->
                                val post = Post(
                                    postId = UUID.randomUUID().toString(),
                                    postCaption = postCaption,
                                    postImage = downloadedUri.toString(),
                                    userId = auth.currentUser?.uid!!
                                )
                                postsRef.document(post.postId).set(post).addOnSuccessListener {
                                    val data = Response(
                                        success = true,
                                        message = context.getString(R.string.posted)
                                    )
                                    trySend(Resource.Success(data))
                                }.addOnFailureListener { exception ->
                                    trySend(
                                        Resource.Failure(
                                            message = exception.localizedMessage ?: context.getString(R.string.unknown_error)
                                        )
                                    )
                                }
                            }.addOnFailureListener { exception ->
                                trySend(
                                    Resource.Failure(
                                        message = exception.localizedMessage ?: context.getString(R.string.unknown_error)
                                    )
                                )
                            }
                        }
                    }
                }
            } else {
                val post = Post(
                    postId = UUID.randomUUID().toString(),
                    postCaption = postCaption,
                    userId = auth.currentUser?.uid!!
                )
                snapshotResult = postsRef.document(post.postId).set(post).addOnSuccessListener {
                    val data = Response(
                        success = true,
                        message = context.getString(R.string.posted)
                    )
                    trySend(Resource.Success(data))
                }.addOnFailureListener { exception ->
                    trySend(
                        Resource.Failure(
                            message = exception.localizedMessage ?: context.getString(R.string.unknown_error)
                        )
                    )
                }
            }
        } else {
            trySend(Resource.Failure(message = context.getString(R.string.signin_required)))
        }

        if (uri != null) {
            awaitClose { uploadSnapshot?.isCanceled }
        } else {
            awaitClose { snapshotResult?.isCanceled }
        }
    }
}