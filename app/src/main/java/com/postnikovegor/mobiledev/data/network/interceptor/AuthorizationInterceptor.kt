package com.postnikovegor.mobiledev.data.network.interceptor

import com.postnikovegor.mobiledev.repository.AuthRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthorizationInterceptor @Inject constructor(
    private val authRepository: AuthRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(
            chain
                .request()
                .newBuilder()
                .apply {
                    runBlocking {
                        authRepository.getAuthTokensFlow().first()?.let {
                            addHeader("Authorization", "Bearer ${it.accessToken}")
                        }
                    }
                }
                .build()
        )
}