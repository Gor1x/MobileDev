package com.postnikovegor.mobiledev.ui.signin

import androidx.lifecycle.viewModelScope
import com.postnikovegor.mobiledev.repository.AuthRepository
import com.postnikovegor.mobiledev.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class SignInViewModel : BaseViewModel() {
    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            AuthRepository.signIn(email, password)
        }
    }
}