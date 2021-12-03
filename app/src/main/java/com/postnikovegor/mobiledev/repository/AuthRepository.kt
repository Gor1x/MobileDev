package com.postnikovegor.mobiledev.repository

import com.postnikovegor.mobiledev.entity.UserRegistrationData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object AuthRepository {
    private val isAuthorizedFlowInner = MutableStateFlow(false)

    val isAuthorizedFlow = isAuthorizedFlowInner.asStateFlow()

    private var isAuthorized: Boolean
        get() = isAuthorizedFlowInner.value
        set(newState) {
            isAuthorizedFlowInner.value = newState
        }

    fun signIn(email: String, password: String) {
        isAuthorized = true
    }

    fun logOut() {
        isAuthorized = false
    }

    fun signUp(
        userRegistrationData: UserRegistrationData
    ) {
        // Do nothing
    }
}