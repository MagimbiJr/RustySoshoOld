package dev.rustybite.rustysosho.domain.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
data class User (
    val userId: String = "",
    val name: String = "",
    val username: String = "",
    val phoneNumber: String = "",
    val dateOfBirth: String = "",
    val gender: String = "",
    val userPhoto: String = "",
)

@RequiresApi(Build.VERSION_CODES.O)
fun DocumentSnapshot.toUser(): User? {
    return try {
        User(
            userId = id,
            name = getString("name")!!,
            username = getString("username")!!,
            phoneNumber = getString("phoneNumber")!!,
            dateOfBirth = getString("dateOfBirth")!!,
            gender = getString("gender")!!,
            userPhoto = getString("userPhoto")!!
        )
    } catch (e: FirebaseFirestoreException) {
        null
    } catch (e: IOException) {
        null
    }
}
