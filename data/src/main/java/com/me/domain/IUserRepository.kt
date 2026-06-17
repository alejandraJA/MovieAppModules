package com.me.domain

interface IUserRepository {
    fun isUserRegistered(): Boolean
    fun registerUser(userName: String, password: String)
    fun loginUser(userName: String, password: String): Boolean
    fun logout()
}