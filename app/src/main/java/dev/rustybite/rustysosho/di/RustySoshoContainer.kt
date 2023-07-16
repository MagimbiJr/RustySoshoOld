package dev.rustybite.rustysosho.di

import com.google.firebase.auth.FirebaseAuth
import dev.rustybite.rustysosho.data.remote.AuthService
import dev.rustybite.rustysosho.domain.repository.AuthRepositoryImpl
import dev.rustybite.rustysosho.presentation.RustySoshoActivity

class RustySoshoContainer(
    //private val context: RustySoshoActivity,
) {
    val auth = FirebaseAuth.getInstance()
    private fun rustySoshoActivity(): RustySoshoActivity = RustySoshoActivity.getInstance() as RustySoshoActivity
    private val api = AuthService(auth, rustySoshoActivity())

    val authRepository = AuthRepositoryImpl(api)
}