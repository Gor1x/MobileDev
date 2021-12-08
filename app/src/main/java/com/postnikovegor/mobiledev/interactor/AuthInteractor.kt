package com.postnikovegor.mobiledev.interactor

import com.haroldadmin.cnradapter.NetworkResponse
import com.postnikovegor.mobiledev.data.network.response.error.SignInWithEmailErrorResponse
import com.postnikovegor.mobiledev.entity.AuthTokens
import com.postnikovegor.mobiledev.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class AuthInteractor @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend fun isAuthorizedFlow(): Flow<Boolean> =
        authRepository.isAuthorizedFlow()

    suspend fun signInWithEmail(
        email: String,
        password: String
    ): NetworkResponse<AuthTokens, SignInWithEmailErrorResponse> {
        val response = authRepository.generateAuthTokensByEmail(email, password)
        when (response) {
            is NetworkResponse.Success -> {
                authRepository.saveAuthTokens(response.body)
            }
            is NetworkResponse.Error -> {
                Timber.e(response.error)
            }
        }
        return response
    }

    suspend fun logout() {
        authRepository.saveAuthTokens(null)
    }
}