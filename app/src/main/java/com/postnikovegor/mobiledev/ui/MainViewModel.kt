package com.postnikovegor.mobiledev.ui

import com.postnikovegor.mobiledev.ui.base.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel : BaseViewModel() {
    val isAuthorizedFlow: Flow<Boolean> = MutableStateFlow(true)
}