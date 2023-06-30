package dev.rustybite.rustysosho.data.repository

import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import dev.rustybite.rustysosho.utils.AuthResponse
import kotlinx.coroutines.flow.MutableStateFlow
import java.lang.Exception

interface AuthRepository {

    /**
     * A state of sign up process. State might be in one of for state:
     * [NotInitialized] - Initial state
     * [Loading] - Sign up process begin
     * [Success] - Sign up process complete with success
     * [Failure] - Sign up process complete with error
     */
    val signUpState: MutableStateFlow<AuthResponse>

    /**
     * It call Firebase service to send SMS.
     */
    fun authenticate(phone: String)

    /**
     * Called when SMS is sent
     */
    fun onCodeSent(
        verificationId: String,
        token: PhoneAuthProvider.ForceResendingToken
    )

    /**
     * Called when the user manually types the SMS codes sent to their phone number
     */
    fun onVerifyOtp(code: String)

    /**
     * Will be called when everything went well
     */
    fun onVerificationCompleted(credential: PhoneAuthCredential)

    /**
     * Will be called when exception occurred
     */
    fun onVerificationFailed(exception: Exception)
    fun getUserPhone(): String

}