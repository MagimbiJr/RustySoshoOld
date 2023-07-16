package dev.rustybite.rustysosho.data.remote

import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dev.rustybite.rustysosho.R
import dev.rustybite.rustysosho.domain.model.Response
import dev.rustybite.rustysosho.presentation.RustySoshoActivity
import dev.rustybite.rustysosho.utils.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.TimeUnit

class AuthService(
    private val auth: FirebaseAuth,
    private val context: RustySoshoActivity,
) {
    var signUpState: Flow<Resource<Response>> = callbackFlow {

    }
        //private set

    val message = MutableStateFlow("")

    var verificationOtp = MutableStateFlow("")
    var resentToken: PhoneAuthProvider.ForceResendingToken? = null


    private val authCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            message.value = context.getString(R.string.verification_complete)
            signInWithCredential(credential)
        }

        override fun onVerificationFailed(exception: FirebaseException) {
            when (exception) {
                is FirebaseAuthInvalidCredentialsException -> {
                    message.value = context.getString(R.string.verification_failed_try_again)
                }

                is FirebaseTooManyRequestsException -> {
                    message.value = context.getString(R.string.quota_exceeded)
                }

                else -> {
                    message.value = exception.localizedMessage
                        ?: context.getString(R.string.unknown_error)
                }
            }
        }

        override fun onCodeSent(code: String, token: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(code, token)
            verificationOtp.value = code
            resentToken = token
            message.value = context.getString(R.string.code_sent)
        }

    }

    private fun signInWithCredential(credential: PhoneAuthCredential): Flow<Resource<out Response>> = callbackFlow {
        val result = auth.signInWithCredential(credential).addOnSuccessListener {
            val data = Response(
                success = true,
                message = context.getString(R.string.phone_auth_success)
            )
            trySend(Resource.Success(data = data))
        }.addOnFailureListener { exception ->
            if (exception is FirebaseAuthInvalidCredentialsException) {
                message.value = context.getString(R.string.invalid_code)
                val response = Response(
                    success = false,
                    message = message.value
                )
                trySend(Resource.Failure(message = response.message))
            } else {
                message.value = exception.localizedMessage
                    ?: context.getString(R.string.unknown_error)
                val response = Response(
                    success = false,
                    message = message.value
                )
                trySend(Resource.Failure(message = response.message))
            }
        }

        awaitClose { result.isCanceled }
    }

    suspend fun authenticate(phone: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setCallbacks(authCallbacks)
            .setActivity(context)
            .setTimeout(120L, TimeUnit.SECONDS)
            .setPhoneNumber(phone)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
        authCallbacks.onCodeSent(verificationId, token)
    }

    fun onVerifyOtp(code: String): Flow<Resource<out Response>> = callbackFlow {
        val credential = PhoneAuthProvider.getCredential(verificationOtp.value, code)
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