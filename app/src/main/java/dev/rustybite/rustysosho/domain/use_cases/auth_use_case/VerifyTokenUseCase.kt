package dev.rustybite.rustysosho.domain.use_cases.auth_use_case

import dev.rustybite.rustysosho.data.repository.AuthRepository

class VerifyTokenUseCase(
    private val repository: AuthRepository
) {

    suspend operator fun invoke(code: String) {
        repository.onVerifyOtp(code)
    }

}