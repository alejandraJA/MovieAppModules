package com.me.domain

import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    val userName: Flow<String>
    val isUserRegistered: Flow<Boolean>

    suspend fun registerUser(userName: String, password: String)
    suspend fun loginUser(userName: String, password: String): Boolean
    suspend fun logout()
}
