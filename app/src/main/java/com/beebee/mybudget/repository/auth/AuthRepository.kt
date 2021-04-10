package com.beebee.mybudget.repository.auth

import com.beebee.mybudget.domain.AuthUser
import com.beebee.mybudget.domain.User
import com.beebee.mybudget.network.AuthService
import com.beebee.mybudget.network.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber

class AuthRepository : IAuthRepository {
    override suspend fun loginUser(email: String, password: String): User {
        var result: User = User()
        withContext(Dispatchers.IO) {
            try {
                val user = AuthUser(email, password)
                val response = AuthService.authService.loginUser(user)
                result = response.asDomainModel()
            } catch (e: HttpException) {
                Timber.e("e ${e.localizedMessage}")
               result = User()
            }
        }
        return result
    }

    override suspend fun registerUser(email: String, password: String): User {
        var result: User = User()
        withContext(Dispatchers.IO) {
            try {
                val user = AuthUser(email, password)
                val response = AuthService.authService.registerUser(user)
                result = response.asDomainModel()
            } catch (e: HttpException) {
                Timber.e("e ${e.localizedMessage}")
                result = User()
            }
        }
        return result
    }
}