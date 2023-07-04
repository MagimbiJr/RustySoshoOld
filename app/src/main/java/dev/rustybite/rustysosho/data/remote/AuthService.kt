package dev.rustybite.rustysosho.data.remote

import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dev.rustybite.rustysosho.R
import dev.rustybite.rustysosho.data.repository.AuthRepository
import dev.rustybite.rustysosho.presentation.RustySoshoActivity
import dev.rustybite.rustysosho.utils.AuthResponse
import dev.rustybite.rustysosho.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.TimeUnit

class AuthService(
    private val auth: FirebaseAuth,
    private val context: RustySoshoActivity,
)  {
    var signUpState: MutableStateFlow<AuthResponse> =
        MutableStateFlow(AuthResponse.NotInitialize)
        private set

    var verificationOtp = ""
    var resentToken: PhoneAuthProvider.ForceResendingToken? = null


    private val authCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signUpState.value = AuthResponse.Success(
                message = context.getString(R.string.verification_complete)
            )
            signInWithCredential(credential)
        }

        override fun onVerificationFailed(exception: FirebaseException) {
            when(exception) {
                is FirebaseAuthInvalidCredentialsException -> {
                    signUpState.value = AuthResponse.Failure(
                        exception = Exception(
                            message = context.getString(R.string.verification_failed_try_again)
                        )
                    )
                }
                is FirebaseTooManyRequestsException -> {
                    signUpState.value = AuthResponse.Failure(
                        exception = Exception(
                            message = context.getString(R.string.quota_exceeded)
                        )
                    )
                }
                else -> {
                    signUpState.value = AuthResponse.Failure(exception)
                }
            }
        }

        override fun onCodeSent(code: String, token: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(code, token)
            verificationOtp = code
            resentToken = token
            signUpState.value = AuthResponse.Success(
                message = context.getString(R.string.code_sent)
            )
            signUpState.value = AuthResponse.Loading(loading = false)
        }

    }

    private val authBuilder = PhoneAuthOptions.newBuilder(auth)
        .setCallbacks(authCallbacks)
        .setActivity(context)
        .setTimeout(120L, TimeUnit.SECONDS)

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnSuccessListener {
            signUpState.value = AuthResponse.Success(
                message = context.getString(R.string.phone_auth_success)
            )
        }.addOnFailureListener { exception ->
            if (exception is FirebaseAuthInvalidCredentialsException) {
                AuthResponse.Failure(
                    exception = Exception(context.getString(R.string.invalid_code))
                )
                return@addOnFailureListener
            } else {
                signUpState.value = AuthResponse.Failure(exception = exception)
            }
        }
    }

    suspend fun authenticate(phone: String) {
        signUpState.value = AuthResponse.Loading(
            loading = true
        )
        val options = authBuilder
            .setPhoneNumber(phone)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
        authCallbacks.onCodeSent(verificationId, token)

    }

    fun onVerifyOtp(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationOtp, code)
        signInWithCredential(credential)
    }

    fun onVerificationCompleted(credential: PhoneAuthCredential) {
        authCallbacks.onVerificationCompleted(credential)
    }

    fun onVerificationFailed(exception: Exception) {
        authCallbacks.onVerificationFailed(exception as FirebaseException)
    }

     fun getUserPhone(): String {
        return auth.currentUser?.uid.orEmpty()
    }


}