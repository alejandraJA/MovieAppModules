package com.me.data

import com.me.data.datasource.Storage
import com.me.domain.Constants
import com.me.domain.IUserRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val storage: Storage) : IUserRepository {
    private var userName: String
        get() = storage.getString(Constants.REGISTERED_USER)?: ""
        set(value) = storage.setString(Constants.REGISTERED_USER, value)

    private var password: String
        get() = storage.getString(Constants.PASSWORD)?: ""
        set(value) = storage.setString(Constants.PASSWORD, value)

    override fun isUserRegistered() = userName.isNotEmpty()

    override fun registerUser(userName: String, password: String) {
        this.userName = userName
        this.password = password
    }

    override fun loginUser(userName: String, password: String) =
        this.userName == userName && this.password == password

    override fun logout() {
        userName = ""
        password = ""
    }

}