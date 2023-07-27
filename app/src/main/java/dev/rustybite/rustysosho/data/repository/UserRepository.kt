package dev.rustybite.rustysosho.data.repository

import android.net.Uri
import dev.rustybite.rustysosho.domain.model.Response
import dev.rustybite.rustysosho.utils.Resource
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun registerUser(
        firstName: String,
        lastName: String,
        phoneNumber: String,
        username: String? = null,
        uri: Uri? = null,
    ): Flow<Resource<out Response>>
}