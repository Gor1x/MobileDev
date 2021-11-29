package com.postnikovegor.mobiledev.data.network

import com.postnikovegor.mobiledev.data.network.response.GetUsersResponse
import retrofit2.http.GET

interface Api {
    @GET("users?page=2")
    suspend fun getUsers(): GetUsersResponse
}
