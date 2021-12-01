package com.postnikovegor.mobiledev.ui

import com.postnikovegor.mobiledev.repository.AuthRepository
import com.postnikovegor.mobiledev.ui.base.BaseViewModel
import kotlinx.coroutines.flow.Flow

class MainViewModel : BaseViewModel() {
    val isAuthorizedFlow: Flow<Boolean> = AuthRepository.isAuthorizedFlow
}