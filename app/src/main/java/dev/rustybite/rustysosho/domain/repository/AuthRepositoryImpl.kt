package dev.rustybite.rustysosho.domain.repository

import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import dev.rustybite.rustysosho.data.remote.AuthService
import dev.rustybite.rustysosho.data.repository.AuthRepository
import dev.rustybite.rustysosho.utils.AuthResponse
import kotlinx.coroutines.flow.MutableStateFlow
import java.lang.Exception

class AuthRepositoryImpl(
    private val api: AuthService,
) : AuthRepository {
    override val signUpState: MutableStateFlow<AuthResponse>
        get() = api.signUpState
    override val verificationOtp: String
        get() = api.verificationOtp
    override val resentToken: PhoneAuthProvider.ForceResendingToken?
        get() = api.resentToken

    override fun authenticate(phone: String) {
        api.authenticate(phone)
    }

    override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
        onCodeSent(verificationId, token)
    }

    override fun onVerifyOtp(code: String) {
        api.onVerifyOtp(code)
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