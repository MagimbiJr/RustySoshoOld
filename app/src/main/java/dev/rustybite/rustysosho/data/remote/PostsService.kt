package dev.rustybite.rustysosho.data.remote

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import dev.rustybite.rustysosho.R
import dev.rustybite.rustysosho.domain.model.Post
import dev.rustybite.rustysosho.domain.model.Response
import dev.rustybite.rustysosho.domain.model.User
import dev.rustybite.rustysosho.domain.model.toUser
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
    private val usersRef = database.collection("users")

    fun addPost(
        postCaption: String,
        uri: Uri?,
        privacyStatus: String
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
                                    userId = auth.currentUser?.uid!!,
                                    privacyStatus = privacyStatus
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
                                            message = exception.localizedMessage
                                                ?: context.getString(R.string.unknown_error)
                                        )
                                    )
                                }
                            }.addOnFailureListener { exception ->
                                trySend(
                                    Resource.Failure(
                                        message = exception.localizedMessage
                                            ?: context.getString(R.string.unknown_error)
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
                    userId = auth.currentUser?.uid!!,
                    privacyStatus = privacyStatus
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
                            message = exception.localizedMessage
                                ?: context.getString(R.string.unknown_error)
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun getUser(): Flow<Resource<out User>> = callbackFlow {
        var snapshotResult: Task<QuerySnapshot>? = null
        if (auth.currentUser != null) {
            snapshotResult = usersRef.whereEqualTo("userId", auth.currentUser?.uid).get()
                .addOnSuccessListener { querySnapshot ->
                    Log.d("TaNa", "getUser: id is ${auth.currentUser?.uid}")
                    if (querySnapshot.documents.isEmpty()) {
                        trySend(
                            Resource.Failure(
                                message = context.getString(R.string.no_user_found)
                            )
                        )
                    } else {
                        var data = User()
                        val users = querySnapshot.documents.mapNotNull { it.toUser() }
                        users.forEach { user ->
                            data = user
                            Log.d("TaNa", "getUser: user is $data")
                        }
                        trySend(Resource.Success(data = data))
                    }
                }.addOnFailureListener { exception ->
                    trySend(
                        Resource.Failure(
                            message = exception.localizedMessage
                                ?: context.getString(R.string.unknown_error)
                        )
                    )
                }
        } else {
            trySend(
                Resource.Failure(message = context.getString(R.string.signin_required))
            )
        }
        awaitClose { snapshotResult?.isCanceled }
    }
}