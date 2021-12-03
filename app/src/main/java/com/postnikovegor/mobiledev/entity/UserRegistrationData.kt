package com.postnikovegor.mobiledev.entity

data class UserRegistrationData(
    val firstname: String,
    val lastname: String,
    val nickname: String,
    val email: String,
    val password: String,
    val termsIsChecked: Boolean
)