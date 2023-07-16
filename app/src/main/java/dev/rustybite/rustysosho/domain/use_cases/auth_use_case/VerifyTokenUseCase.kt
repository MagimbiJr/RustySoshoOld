package dev.rustybite.rustysosho.domain.use_cases.auth_use_case

import dev.rustybite.rustysosho.data.repository.AuthRepository
import dev.rustybite.rustysosho.domain.model.Response
import dev.rustybite.rustysosho.utils.Resource
import kotlinx.coroutines.flow.Flow

class VerifyTokenUseCase(
    private val repository: AuthRepository
) {

    suspend operator fun invoke(code: String): Flow<Resource<out Response>> {
        return repository.onVerifyOtp(code)
    }

}