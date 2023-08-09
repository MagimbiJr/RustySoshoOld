package dev.rustybite.rustysosho.data.repository

import android.net.Uri
import dev.rustybite.rustysosho.domain.model.Response
import dev.rustybite.rustysosho.utils.Resource
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun registerUser(
        name: String,
        username: String? = null,
        uri: Uri? = null,
        gender: String,
        birthDate: String
    ): Flow<Resource<out Response>>
}