package com.postnikovegor.mobiledev.data.network

import com.postnikovegor.mobiledev.data.network.response.GetUsersResponse
import retrofit2.http.GET

interface Api {
    @GET("users?per_page=100")
    suspend fun getUsers(): GetUsersResponse
}
