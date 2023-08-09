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
        name: String,
        username: String?,
        uri: Uri?,
        gender: String,
        birthDate: String
    ): Flow<Resource<out Response>> {
        return api.createUser(name, username, uri, birthDate, gender)
    }
}