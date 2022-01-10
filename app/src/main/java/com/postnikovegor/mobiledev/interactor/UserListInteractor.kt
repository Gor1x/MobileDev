package com.postnikovegor.mobiledev.interactor

import com.postnikovegor.mobiledev.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserListInteractor @Inject constructor(
    //private val api: Api
) {
    suspend fun sendLoadUsersRequest(): Result<List<User>> =
        withContext(Dispatchers.IO) {
            runCatching {
                emptyList<User>()
                //api.getUsers().data
            }
        }
}