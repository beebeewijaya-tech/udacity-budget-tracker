package com.beebee.mybudget.repository.auth

import com.beebee.mybudget.domain.User

interface IAuthRepository {
    suspend fun loginUser(email: String, password: String) : User
    suspend fun registerUser(email: String, password: String) : User
}