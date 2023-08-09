package dev.rustybite.rustysosho.data.remote

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import dev.rustybite.rustysosho.R
import dev.rustybite.rustysosho.domain.model.Response
import dev.rustybite.rustysosho.utils.Resource
import dev.rustybite.rustysosho.utils.ResourceProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class UserService(
    private val database: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val auth: FirebaseAuth,
    private val resProvider: ResourceProvider
) {
    private val usersRef = database.collection("users")
    private val storageRef = storage.reference.child("images")

    suspend fun createUser(
        name: String,
        username: String? = null,
        uri: Uri? = null,
        gender: String,
        birthDate: String
    ): Flow<Resource<out Response>> = callbackFlow {
        var storageResultSnapshot: StorageTask<UploadTask.TaskSnapshot>? = null
        var usersResultSnapshot: Task<Void>? = null
        val number = auth.currentUser?.phoneNumber ?: ""
        usersRef.whereEqualTo("phoneNumber", number).get().addOnSuccessListener { querySnapshot ->
            if (querySnapshot.documents.isEmpty()) {
                if (uri !=  null) {
                    val valuedUserName = username ?: ""
                    storageResultSnapshot = storageRef.putFile(uri).addOnCompleteListener { uploadTask ->
                        if (uploadTask.isSuccessful) {
                            if (uploadTask.isComplete) {
                                storageRef.downloadUrl.addOnSuccessListener { downloadedUri ->
                                    val user = hashMapOf(
                                        "name" to name,
                                        "phoneNumber" to number,
                                        "username" to valuedUserName,
                                        "birthDate" to birthDate,
                                        "gender" to gender,
                                        "userImage" to downloadedUri.toString()
                                    )
                                    usersRef.document(auth.currentUser?.uid.toString()).set(user).addOnSuccessListener {
                                        val data = Response(
                                            success = true,
                                            message = resProvider.getString(R.string.user_registered_successful)
                                        )
                                        trySend(Resource.Success(data))
                                    }.addOnFailureListener { exception ->
                                        trySend(
                                            Resource.Failure(
                                                message = exception.localizedMessage ?: resProvider.getString(R.string.unknown_error)
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    val valuedUserName = username ?: ""
                    val user = hashMapOf(
                        "name" to name,
                        "phoneNumber" to number,
                        "username" to valuedUserName,
                        "birthDate" to  birthDate,
                        "gender" to gender,
                        "userImage" to ""
                    )
                    usersResultSnapshot = usersRef.document(auth.currentUser?.uid.toString()).set(user).addOnSuccessListener {
                        val data = Response(
                            success = true,
                            message = resProvider.getString(R.string.user_registered_successful)
                        )
                        trySend(Resource.Success(data))
                    }.addOnFailureListener { exception ->
                        trySend(
                            Resource.Failure(
                                message = exception.localizedMessage ?: resProvider.getString(R.string.unknown_error)
                            )
                        )
                    }
                }
            } else {
                trySend(
                    Resource.Failure(
                        message = resProvider.getString(R.string.phone_number_used)
                    )
                )
            }
        }.addOnFailureListener { exception ->
            trySend(
                Resource.Failure(
                    message = exception.localizedMessage ?: resProvider.getString(R.string.unknown_error)
                )
            )
        }

        awaitClose {
            if (uri != null) {
                storageResultSnapshot?.cancel()
            } else {
                usersResultSnapshot?.isCanceled
            }
        }
    }
}