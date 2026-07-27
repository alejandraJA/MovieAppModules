package com.me.data

import com.me.data.datasource.Storage
import com.me.domain.Constants
import com.me.domain.IUserRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class UserRepository @Inject constructor(private val storage: Storage) : IUserRepository {
    override val userName: Flow<String> =
        storage.getStringFlow(Constants.REGISTERED_USER)
            .map { it.orEmpty() }

    override val isUserRegistered: Flow<Boolean> =
        userName
            .map { userName -> !userName.isNullOrEmpty() }

    override suspend fun registerUser(userName: String, password: String) {
        storage.setStrings(
            mapOf(
                Constants.REGISTERED_USER to userName,
                Constants.PASSWORD to password,
            )
        )
    }

    override suspend fun loginUser(userName: String, password: String): Boolean {
        val registeredUserName = storage.getString(Constants.REGISTERED_USER).orEmpty()
        val registeredPassword = storage.getString(Constants.PASSWORD).orEmpty()

        return registeredUserName == userName && registeredPassword == password
    }

    override suspend fun logout() {
        storage.setStrings(
            mapOf(
                Constants.REGISTERED_USER to "",
                Constants.PASSWORD to "",
            )
        )
    }

}
