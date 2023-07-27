package dev.rustybite.rustysosho.domain.repository

import android.net.Uri
import dev.rustybite.rustysosho.data.remote.UserService
import dev.rustybite.rustysosho.data.repository.UserRepository
import dev.rustybite.rustysosho.domain.model.Response
import dev.rustybite.rustysosho.utils.Resource
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(
    private val api: UserService
) : UserRepository {
    override suspend fun registerUser(
        firstName: String,
        lastName: String,
        phoneNumber: String,
        username: String?,
        uri: Uri?
    ): Flow<Resource<out Response>> {
        return api.createUser(firstName, lastName, phoneNumber, username, uri)
    }
}