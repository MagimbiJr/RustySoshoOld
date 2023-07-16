package dev.rustybite.rustysosho.domain.repository

import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import dev.rustybite.rustysosho.data.remote.AuthService
import dev.rustybite.rustysosho.data.repository.AuthRepository
import dev.rustybite.rustysosho.domain.model.Response
import dev.rustybite.rustysosho.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class AuthRepositoryImpl(
    private val api: AuthService,
) : AuthRepository {

//    override val signUpState: MutableStateFlow<Response>
//        get() = api.signUpState
    override val verificationOtp: MutableStateFlow<String>
        get() = api.verificationOtp
    override val resentToken: PhoneAuthProvider.ForceResendingToken?
        get() = api.resentToken

    override val message: MutableStateFlow<String>
        get() = api.message
    override suspend fun authenticate(phone: String) {
        api.authenticate(phone)
    }

    override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
        onCodeSent(verificationId, token)
    }

    override fun onVerifyOtp(code: String): Flow<Resource<out Response>> {
        return api.onVerifyOtp(code)
    }

    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
        api.onVerificationCompleted(credential)
    }

    override fun onVerificationFailed(exception: Exception) {
        api.onVerificationFailed(exception)
    }

    override fun getUserPhone(): String {
       return api.getUserPhone()
    }
}