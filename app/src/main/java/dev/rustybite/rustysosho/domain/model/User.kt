package dev.rustybite.rustysosho.domain.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import java.io.IOException
import java.time.LocalDateTime
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.O)
data class User (
    val userId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val username: String = "",
    val phoneNumber: String = "",
    val dateOfBirth: String = LocalDateTime.now().toString(),
    val userPhoto: String = "",
)

@RequiresApi(Build.VERSION_CODES.O)
fun DocumentSnapshot.toUser(): User? {
    return try {
        User(
            userId = id,
            firstName = getString("firstName")!!,
            lastName = getString("lastName")!!,
            username = getString("username")!!,
            phoneNumber = getString("phoneNumber")!!,
            dateOfBirth = getString("dateOfBirth")!!,
            userPhoto = getString("userPhoto")!!
        )
    } catch (e: FirebaseFirestoreException) {
        null
    } catch (e: IOException) {
        null
    }
}
