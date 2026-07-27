package com.me.daggerhilt.ui.theme.sing

import androidx.lifecycle.ViewModel
import com.me.domain.IUserRepository
import com.me.domain.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

@HiltViewModel
class SingInViewModel @Inject constructor(
    private val userRepository: IUserRepository
) : ViewModel() {
    val userRegisteredState: Flow<UiState<Boolean>> =
        userRepository.isUserRegistered
            .map<Boolean, UiState<Boolean>> { isRegistered -> UiState.Success(isRegistered) }
            .onStart { emit(UiState.Loading()) }
            .catch { emit(UiState.Error(it.message ?: "Unknown error")) }

    suspend fun login(user: String, password: String) =
        userRepository.loginUser(user, password)

    suspend fun registerUser(user: String, password: String) {
        userRepository.registerUser(user, password)
    }

}
