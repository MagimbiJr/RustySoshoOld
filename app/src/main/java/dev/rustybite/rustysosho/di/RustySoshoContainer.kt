package dev.rustybite.rustysosho.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dev.rustybite.rustysosho.data.remote.AuthService
import dev.rustybite.rustysosho.data.remote.UserService
import dev.rustybite.rustysosho.domain.repository.AuthRepositoryImpl
import dev.rustybite.rustysosho.domain.repository.UserRepositoryImpl
import dev.rustybite.rustysosho.presentation.RustySoshoActivity
import dev.rustybite.rustysosho.utils.ResourceProvider

class RustySoshoContainer(
    //private val context: RustySoshoActivity,
) {
    val resProvider = ResourceProvider(rustySoshoActivity().resources)
    val auth = FirebaseAuth.getInstance()
    private val database = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private fun rustySoshoActivity(): RustySoshoActivity = RustySoshoActivity.getInstance() as RustySoshoActivity
    private val authService = AuthService(auth, database, rustySoshoActivity())
    private val userService = UserService(database, storage, auth, resProvider,)

    val authRepository = AuthRepositoryImpl(authService)
    val userRepository = UserRepositoryImpl(userService)
}