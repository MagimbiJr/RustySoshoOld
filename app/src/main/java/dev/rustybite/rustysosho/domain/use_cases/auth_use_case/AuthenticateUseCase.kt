package dev.rustybite.rustysosho.domain.use_cases.auth_use_case

import dev.rustybite.rustysosho.data.repository.AuthRepository

class AuthenticateUseCase(
    private val repository: AuthRepository
) {

    suspend operator fun invoke(phone: String) {
        repository.authenticate(phone)
    }

}