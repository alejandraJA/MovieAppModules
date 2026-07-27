package com.me.daggerhilt.ui.sing

import androidx.lifecycle.ViewModel
import com.me.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SingInViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    val isUserRegistered: Boolean
        get() = userRepository.isUserRegistered()

    fun login(user: String, password: String) =
        userRepository.loginUser(user, password)

    fun registerUser(user: String, password: String) {
        userRepository.registerUser(user, password)
    }

}