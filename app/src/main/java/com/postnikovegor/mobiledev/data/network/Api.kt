package com.postnikovegor.mobiledev.data.network

import com.haroldadmin.cnradapter.NetworkResponse
import com.postnikovegor.mobiledev.data.network.request.CreateProfileRequest
import com.postnikovegor.mobiledev.data.network.request.RefreshAuthTokensRequest
import com.postnikovegor.mobiledev.data.network.request.SignInWithEmailRequest
import com.postnikovegor.mobiledev.data.network.response.GetUsersResponse
import com.postnikovegor.mobiledev.data.network.response.VerificationTokenResponse
import com.postnikovegor.mobiledev.data.network.response.error.*
import com.postnikovegor.mobiledev.entity.AuthTokens
import retrofit2.http.*

interface Api {
    @GET("users?per_page=10")
    suspend fun getUsers(): GetUsersResponse

    @POST("auth/sign-in-email")
    suspend fun signInWithEmail(
        @Body request: SignInWithEmailRequest
    ): NetworkResponse<AuthTokens, SignInWithEmailErrorResponse>

    @POST("auth/refresh-tokens")
    suspend fun refreshAuthTokens(
        @Body request: RefreshAuthTokensRequest
    ): NetworkResponse<AuthTokens, RefreshAuthTokensErrorResponse>

    @POST("registration/verification-code/send")
    suspend fun sendRegistrationVerificationCode(
        @Query("email") email: String,
    ): NetworkResponse<Unit, SendRegistrationVerificationCodeErrorResponse>

    @POST("registration/verification-code/verify")
    suspend fun verifyRegistrationCode(
        @Query("code") code: String,
        @Query("email") email: String?,
        @Query("phone_number") phoneNumber: String?
    ): NetworkResponse<VerificationTokenResponse, VerifyRegistrationCodeErrorResponse>

    @PUT("registration/create-profile")
    suspend fun createProfile(
        @Body request: CreateProfileRequest
    ): NetworkResponse<AuthTokens, CreateProfileErrorResponse>
}
